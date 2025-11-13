import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserRole } from '../../models/user.model';
import { DashboardService } from '../../services/dashboard.service';
import { DashboardStats } from '../../models/dashboard-stats.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-manager-dashboard',
  standalone: false, // <-- Ensure this is set to false
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.css']
})
export class ManagerDashboardComponent implements OnInit {

  stats$?: Observable<DashboardStats>;
  protected readonly role: typeof UserRole = UserRole;

  constructor(
    protected authService: AuthService,
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    // Only fetch stats if the user is a manager
    if (this.authService.getCurrentUser()?.role === UserRole.MANAGER) {
      this.stats$ = this.dashboardService.getStats();
    }
  }
}
