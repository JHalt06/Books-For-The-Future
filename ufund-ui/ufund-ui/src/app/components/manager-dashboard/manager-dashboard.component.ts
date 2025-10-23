import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {CupboardService} from '../../services/cupboard.service';
import {UsersService} from '../../services/users.service';
import {BehaviorSubject} from 'rxjs';
import {Need} from '../../models/need.model';
import {UserRole} from '../../models/user.model';

@Component({
    selector: 'app-dashboard',
    standalone: false,
    templateUrl: './manager-dashboard.component.html',
    styleUrl: './manager-dashboard.component.css'
})
export class ManagerDashboardComponent implements OnInit{

    protected userCount = new BehaviorSubject<number | undefined>(undefined)
    protected totalFunding = new BehaviorSubject<String | undefined>(undefined)
    protected totalNeeds = new BehaviorSubject<number | undefined>(undefined)
    protected completedNeeds = new BehaviorSubject<Need[] | undefined>(undefined)
    role: typeof UserRole = UserRole

    constructor(
        protected authService: AuthService,
        protected router: Router,
        protected cupboardService: CupboardService,
        protected userService: UsersService
    ) {}

    ngOnInit() {
        this.cupboardService.getNeeds().subscribe(needs => {
            let totalValue = 0
            for (let need of needs) {
              totalValue += need.fundingAmount
              this.totalFunding.next(totalValue.toLocaleString())
            }
            this.completedNeeds.next(needs.filter(a => ((a.fundingAmount / a.quantity)) >= 1))
            this.totalNeeds.next(needs.length)
            needs = needs.filter(a => a.fundingAmount != 0)
        })
    }

    protected readonly UserRole = UserRole;
}