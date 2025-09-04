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
import { LoadingComponent } from '@shared/components/loading/loading.component';

@Component({
  selector: 'user-person-management',
  imports: [CommonModule, FeedbackMessageComponent, IconComponent,
    LoadingComponent],
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
  personalProfile = signal<Person | null>(null);
  isLoadingPerson = signal(false);
  isLoadingUser = signal(false);

  ngOnInit() {
    this.feedbackService.clearMessage();
    this.loadUserFromRoute();

    // Listen for route parameter changes to reload personal profile when returning to this component
    this.routerSubscription = this.route.params.subscribe((params) => {
      const userId = params['id'];
      if (userId && userId !== 'new' && this.currentUser()?.id === userId) {
        // Reload personal profile when returning to this component
        this.loadPersonalProfile();
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
      this.loadPersonalProfile();
    } catch (error) {
      this.feedbackService.showError('User not found');
      this.router.navigate(['/admin/users']);
    } finally {
      this.isLoadingUser.set(false);
    }
  }

  private async loadPersonalProfile() {
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

      const personalProfile = personsResponse.data.find(person => person.userId === user.id);
      this.personalProfile.set(personalProfile || null);
    } catch (error) {
      this.personalProfile.set(null);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  goBack() {
    this.feedbackService.clearMessage();
    this.navigationHistory.goBackToUser(this.currentUser()?.id || '');
  }

  editPerson() {
    const person = this.personalProfile();
    if (person) {
      this.router.navigate(['/admin/persons', person.id]);
    }
  }

  removePersonalProfile() {
    const person = this.personalProfile();
    const user = this.currentUser();
    if (!person || !user) return;

    this.performRemoval();
  }

  private async performRemoval() {
    const person = this.personalProfile();
    if (!person) return;

    this.isLoadingPerson.set(true);
    try {
      await this.personAssociationService.removePersonalProfile(person.id, `${person.firstName} ${person.lastName}`);
      this.personalProfile.set(null);
    } finally {
      this.isLoadingPerson.set(false);
    }
  }

  linkExistingPerson() {
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
    const person = this.personalProfile();
    const user = this.currentUser();
    if (!person || !user) return;

    await this.performChangeAssociation();
  }

  private async performChangeAssociation() {
    const person = this.personalProfile();
    const user = this.currentUser();
    if (!person || !user) return;

    this.isLoadingPerson.set(true);
    try {
      await this.personAssociationService.changePersonAssociation(person.id, user.id);
      this.personalProfile.set(null);

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
