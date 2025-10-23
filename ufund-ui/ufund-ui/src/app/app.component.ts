import {Component, OnInit, Inject} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {DOCUMENT, Location} from '@angular/common';
import {AuthService} from './services/auth.service';
import {User, UserRole} from './models/user.model';
import {Router} from '@angular/router';
import {ModalService} from './services/modal.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
    
    title = 'ufund-ui';
    currentUser?: BehaviorSubject<User | null>;
    role: typeof UserRole = UserRole

    constructor(
        private authService: AuthService,
        private router: Router,
        protected location: Location,
        protected modalService: ModalService,
        @Inject(DOCUMENT) private document: Document
    ) {}

    reloadPage() {
        this.document.defaultView?.location.reload();
    }

    ngOnInit() {
        this.currentUser = this.authService.getCurrentUserObj()
        let data = localStorage.getItem("credential");
        if (data) {
            let dataParsed = JSON.parse(data)
            this.authService.restoreLogin(dataParsed.username)
        }
        this.document.body.parentElement!.setAttribute("theme","default");
    }

    login() {
        this.router.navigate(['/login'], {queryParams: {redir: this.router.url}});
    }

    logout() {
        localStorage.removeItem("credential")
        location.reload()
    }
}
