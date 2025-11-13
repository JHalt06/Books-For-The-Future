
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ManagerDashboardComponent } from './manager-dashboard.component';
import { AuthService } from '../../services/auth.service';
import { DashboardService } from '../../services/dashboard.service';
import { of } from 'rxjs';
import { UserRole } from '../../models/user.model';

describe('ManagerDashboardComponent', () => {
  let component: ManagerDashboardComponent;
  let fixture: ComponentFixture<ManagerDashboardComponent>;

  // Mock AuthService
  const authServiceMock = {
    getCurrentUser: () => ({
      username: 'admin',
      role: UserRole.MANAGER,
      basket: []
    })
  };

  // Mock DashboardService
  const dashboardServiceMock = {
    getStats: () => of({ totalUsers: 10, totalNeeds: 50, totalFunding: 12345 })
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManagerDashboardComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: DashboardService, useValue: dashboardServiceMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ManagerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
