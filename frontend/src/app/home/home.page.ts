import { Component } from '@angular/core';
import { User } from '../core/models/user.model';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false,
})
export class HomePage {
  currentUser: User | null = null;

  constructor(private authService: AuthService) {}

  ionViewWillEnter(): void {
    this.currentUser = this.authService.getCurrentUser();
  }

}
