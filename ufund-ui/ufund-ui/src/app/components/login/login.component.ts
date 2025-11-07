import {Component, OnInit} from '@angular/core'
import {UsersService} from '../../services/users.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import { UserRole } from '../../models/user.model';

@Component({
    selector: 'app-login',
    standalone: false,
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})

export class LoginComponent {
    loginFailed = false

    constructor(
        protected usersService: UsersService,
        private authService: AuthService,
        protected router: Router,
        private route: ActivatedRoute
    ) {}

    login(username: string | null, password: string | null) {
        this.loginFailed = false;
        let next = '/'
        console.log(`Login req, user : pass ${username} ${password}`)
        if (!username || !password) {
            return;
        }
        this.authService.login(username, password).then(() => {
            if (this.authService.getCurrentUser()?.role == UserRole.MANAGER) {
                next = '/dashboard'
            }
            this.router.navigate([next]);
            localStorage.setItem("credential", JSON.stringify({username: username, password: password}))
            
        }).catch(ex => {
            this.loginFailed = true;
            console.log(ex)
        })
    }
}