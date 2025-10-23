import {Component, OnInit} from '@angular/core'
import {UsersService} from '../../services/users.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';

@Component({
    selector: 'app-login',
    standalone: false,
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})

export class LoginComponent {

    constructor(
        protected usersService: UsersService,
        private authService: AuthService,
        protected router: Router,
        private route: ActivatedRoute
    ) {}

    login(username: string | null, password: string | null) {
        let next = '/dashboard'
        console.log(`Login req, user : pass ${username} ${password}`)
        if (!username || !password) {
          return;
        }

        this.authService.login(username, password).then(() => {
            this.router.navigate([next]);
            localStorage.setItem("credential", JSON.stringify({username: username, password: password}))
            
        }).catch(ex => {
            console.log(ex)
        })
    }
}