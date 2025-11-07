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
        // if(!username || !password) return;
        // this.authService.login(username,password).then(user => {
        //     let next = user.role === 'HELPER' ? '/cupboard' : '/manager-dashboard';
        //     this.router.navigate([next]); 

        //     localStorage.setItem("credential", JSON.stringify({username, password}));
        // })
        // .catch(err => {
        //     console.log(err);

        //     alert('Incorrect username or password');
        // })
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