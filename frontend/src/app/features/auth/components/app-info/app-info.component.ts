import { Component } from '@angular/core';

@Component({
  selector: 'app-app-info',
  templateUrl: './app-info.component.html',
})
export class AppInfoComponent {

  appName = 'Veraz';
  appTitle = 'Your task management platform';
  appDescription = 'Organize, prioritize and complete your tasks efficiently. An intuitive experience to maximize your productivity.';

  features = [
    'Intelligent task management',
    'Real-time collaboration',
    'Productivity analytics'
  ];
}
