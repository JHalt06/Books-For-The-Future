import {Component, Input, OnInit} from '@angular/core';
import {Need} from '../../models/need.model';
import {ActivatedRoute, Router} from "@angular/router";
import {CupboardService} from "../../services/cupboard.service";
import {AuthService} from '../../services/auth.service';
import {catchError, of} from 'rxjs';
import {UsersService} from '../../services/users.service';
import { ModalService } from '../../services/modal.service';

@Component({
    selector: 'app-need-info',
    standalone: false,
    templateUrl: './need-info.component.html',
    styleUrl: './need-info.component.css'
})

export class NeedInfoComponent implements OnInit {

    constructor(
        private route: ActivatedRoute,
        private cupboardService: CupboardService,
        private authService: AuthService,
        protected usersService: UsersService,
        protected router: Router,
        protected modalService: ModalService
    ) {}

    @Input() need!: Need;
    warned: boolean = false;

    ngOnInit(): void {
        const id = Number(this.route.snapshot.paramMap.get('id'));
        console.log('Requesting need ID:', id);
        this.cupboardService.getNeed(id).subscribe(n => this.need = n);
    }

    add(need: Need) {
        const currentUser = this.authService.getCurrentUser();
        if (currentUser) {
            if (!currentUser.basket.includes(need.id)) {
                currentUser.basket.push(need.id);
                this.usersService.updateUser(currentUser)
                    .pipe(catchError((err, _) =>  {
                        return of();
                    }))
                    .subscribe(() => {
                        this.usersService.refreshBasket();
                    });
            }
        }
    }

    delete(id : number) {
        this.cupboardService.deleteNeed(id)
            .pipe(catchError((ex, _) => {
                return of()
            }))
            .subscribe(() => {
                this.router.navigate(['/cupboard'])
            })
    }
}
