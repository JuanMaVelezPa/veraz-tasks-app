import { Component, effect, inject } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';
import { PersonService } from '@person/services/person.service';
import { PersonDetailsComponent } from "./person-details/person-details.component";
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-person-admin-page',
  imports: [PersonDetailsComponent, LoadingComponent, IconComponent],
  templateUrl: './person-admin-page.component.html',
})
export class PersonAdminPageComponent {

  activatedRoute = inject(ActivatedRoute);
  router = inject(Router);
  personService = inject(PersonService);

  personId = toSignal(
    this.activatedRoute.params.pipe(
      map((params) => params['id'])
    )
  )

  personResource = rxResource(
    {
      params: () => ({ id: this.personId() }),
      stream: ({ params }) => this.personService.getPersonById(params.id),
    }
  )

  redirectEffect = effect(() => {
    if (this.personResource.error()) {
      this.router.navigate(['/admin/persons']);
    }
  })

}
