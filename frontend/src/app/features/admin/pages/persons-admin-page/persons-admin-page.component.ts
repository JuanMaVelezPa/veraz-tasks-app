import { Component } from '@angular/core';
import { PersonTableComponent } from '@person/components/person-table/person-table.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-persons-admin-page',
  imports: [PersonTableComponent, RouterLink],
  templateUrl: './persons-admin-page.component.html',
})
export class PersonsAdminPageComponent {
}
