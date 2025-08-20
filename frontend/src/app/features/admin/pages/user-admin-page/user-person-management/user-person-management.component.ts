import { Component, inject, input, signal, OnDestroy, OnInit } from '@angular/core';
import { User } from '@users/interfaces/user.interface';
import { Person } from '@person/interfaces/person.interface';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '@users/services/user.service';
import { PersonService } from '@person/services/person.service';
import { CommonModule } from '@angular/common';
import { FeedbackMessageComponent } from '@shared/components/feedback-message/feedback-message.component';
import { FeedbackMessageService } from '@shared/services/feedback-message.service';
import { NavigationHistoryService } from '@shared/services/navigation-history.service';
import { PersonAssociationService } from '@person/services/person-association.service';
import { IconComponent } from '@shared/components/icon/icon.component';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'user-person-management',
  imports: [CommonModule, FeedbackMessageComponent, IconComponent],
  templateUrl: './user-person-management.component.html',
})
export class UserPersonManagementComponent implements OnInit, OnDestroy {

  router = inject(Router);
  route = inject(ActivatedRoute);
  userService = inject(UserService);
  personService = inject(PersonService);
  feedbackService = inject(FeedbackMessageService);
  navigationHistory = inject(NavigationHistoryService);
  personAssociationService = inject(PersonAssociationService);

  private routerSubscription: any;

  // Signals
  currentUser = signal<User | null>(null);
  associatedPerson = signal<Person | null>(null);
  isLoadingPerson = signal(false);
  isLoadingUser = signal(false);

  ngOnInit() {
    this.feedbackService.clearMessage();
    this.loadUserFromRoute();

    // Listen for route parameter changes to reload associated person when returning to this component
    this.routerSubscription = this.route.params.subscribe((params) => {
      const userId = params['id'];
      if (userId && userId !== 'new' && this.currentUser()?.id === userId) {
        // Reload associated person when returning to this component
        this.loadAssociatedPerson();
      }
    });
  }

  private async loadUserFromRoute() {
    const userId = this.route.snapshot.paramMap.get('id');
    if (!userId || userId === 'new') {
      this.router.navigate(['/admin/users']);
      return;
    }

    this.isLoadingUser.set(true);
    try {
      const user = await firstValueFrom(this.userService.getUserById(userId));
      this.currentUser.set(user);
      this.loadAssociatedPerson();
    } catch (error) {
      this.feedbackService.showError('User not found');
      this.router.navigate(['/admin/users']);
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  private async loadAssociatedPerson() {
    const user = this.currentUser();
    if (!user || user.id === 'new') {
      return;
    }

    this.isLoadingPerson.set(true);

    try {
      const searchOptions = { page: 0, size: 1000, sort: 'id', order: 'asc' as const, search: '' };
      const personsResponse = await firstValueFrom(
        this.personService.getPersons(searchOptions)
      );

      const associatedPerson = personsResponse.data.find(person => person.userId === user.id);
      this.associatedPerson.set(associatedPerson || null);
    } catch (error) {
      console.error('Error loading associated person:', error);
      this.associatedPerson.set(null);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }



  goBack() {
    this.feedbackService.clearMessage();
    this.navigationHistory.goBackToUser(this.currentUser()?.id || '');
  }



  editPerson() {
    const person = this.associatedPerson();
    if (person) {
      this.router.navigate(['/admin/persons', person.id]);
    }
  }

  disassociatePerson() {
    const person = this.associatedPerson();
    const user = this.currentUser();
    if (!person || !user) return;

    this.performDisassociation();
  }

  private async performDisassociation() {
    const person = this.associatedPerson();
    if (!person) return;

    this.isLoadingPerson.set(true);
    try {
      await this.personAssociationService.disassociatePerson(person.id, `${person.firstName} ${person.lastName}`);
      this.associatedPerson.set(null);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  associateExistingPerson() {
    this.router.navigate(['/admin/persons'], {
      queryParams: {
        mode: 'select',
        userId: this.currentUser()?.id
      }
    });
  }

  createNewPerson() {
    this.router.navigate(['/admin/persons/new'], {
      queryParams: {
        userId: this.currentUser()?.id
      }
    });
  }

  async changePerson() {
    const person = this.associatedPerson();
    const user = this.currentUser();
    if (!person || !user) return;

    await this.performChangeAssociation();
  }

  private async performChangeAssociation() {
    const person = this.associatedPerson();
    const user = this.currentUser();
    if (!person || !user) return;

    this.isLoadingPerson.set(true);
    try {
      await this.personAssociationService.changePersonAssociation(person.id, user.id);
      this.associatedPerson.set(null);

      // Navigate to person selection
      this.router.navigate(['/admin/persons'], {
        queryParams: {
          mode: 'select',
          userId: user.id
        }
      });
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  ngOnDestroy() {
    this.feedbackService.clearMessage();
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }
}
