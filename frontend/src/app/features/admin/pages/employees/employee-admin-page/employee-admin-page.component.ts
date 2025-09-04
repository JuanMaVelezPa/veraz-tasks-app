import { Component, effect, inject } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';
import { EmployeeService } from '@employee/services/employee.service';
import { EmployeeDetailsComponent } from "./employee-details/employee-details.component";
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { IconComponent } from '@shared/components/icon/icon.component';

@Component({
  selector: 'app-employee-admin-page',
  standalone: true,
  imports: [EmployeeDetailsComponent, LoadingComponent, IconComponent],
  templateUrl: './employee-admin-page.component.html',
})
export class EmployeeAdminPageComponent {

  activatedRoute = inject(ActivatedRoute);
  router = inject(Router);
  employeeService = inject(EmployeeService);

  employeeId = toSignal(
    this.activatedRoute.params.pipe(
      map((params) => params['id'])
    )
  )

  employeeResource = rxResource(
    {
      params: () => ({ id: this.employeeId() }),
      stream: ({ params }) => this.employeeService.getEmployeeById(params.id),
    }
  )

  redirectEffect = effect(() => {
    if (this.employeeResource.error()) {
      this.router.navigate(['/admin/employees']);
    }
  })
}
