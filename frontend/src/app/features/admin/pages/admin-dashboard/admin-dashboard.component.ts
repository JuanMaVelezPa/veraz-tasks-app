import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { IconComponent } from '@shared/components/icon/icon.component';
import { IconType } from '@shared/constants/icons.constant';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, IconComponent],
  templateUrl: './admin-dashboard.component.html',
})
export class AdminDashboardComponent {
  private router = inject(Router);

  // Quick action cards
  quickActions: Array<{
    title: string;
    description: string;
    icon: IconType;
    route: string;
    color: string;
    badge?: string;
  }> = [
    {
      title: 'Manage Users',
      description: 'View and manage system users',
      icon: 'settings',
      route: '/admin/users',
      color: 'btn-secondary'
    },
    {
      title: 'Manage People',
      description: 'View and manage people records',
      icon: 'users',
      route: '/admin/persons',
      color: 'btn-accent'
    }
  ];

  // Statistics cards (placeholder for future implementation)
  statistics: Array<{
    title: string;
    value: string;
    icon: IconType;
    color: string;
  }> = [
    {
      title: 'Total Users',
      value: '12',
      icon: 'settings',
      color: 'text-primary'
    },
    {
      title: 'Total People',
      value: '45',
      icon: 'users',
      color: 'text-secondary'
    },
    {

      title: 'Active Sessions',
      value: '3',
      icon: 'signal',
      color: 'text-info'
    }
  ];
}
