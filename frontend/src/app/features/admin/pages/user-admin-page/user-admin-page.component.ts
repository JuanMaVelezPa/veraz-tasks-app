import { Component, effect, inject } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';
import { UserService } from '@users/services/user.service';
import { UserDetailsComponent } from "./user-details/user-details.component";
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-user-admin-page',
  imports: [UserDetailsComponent, LoadingComponent, IconComponent],
  templateUrl: './user-admin-page.component.html',
})
export class UserAdminPageComponent {

  activatedRoute = inject(ActivatedRoute);
  router = inject(Router);
  userService = inject(UserService);

  userId = toSignal(
    this.activatedRoute.params.pipe(
      map((params) => params['id'])
    )
  )

  userResource = rxResource(
    {
      params: () => ({ id: this.userId() }),
      stream: ({ params }) => this.userService.getUserById(params.id),
    }
  )

  redirectEffect = effect(() => {
    if (this.userResource.error()) {
      this.router.navigate(['/admin/users']);
    }
  })

}
