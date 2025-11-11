import {Component, OnInit, Inject, OnDestroy} from '@angular/core';
import {BehaviorSubject, Subscription, interval} from 'rxjs';
import {DOCUMENT, Location} from '@angular/common';
import {AuthService} from './services/auth.service';
import {User, UserRole} from './models/user.model';
import {Router} from '@angular/router';
import {ModalService} from './services/modal.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {

  title = 'ufund-ui';
  currentUser?: BehaviorSubject<User | null>;
  role: typeof UserRole = UserRole

  // Notification properties
  unreadCount: number = 0;
  notifications: string[] = [];
  showNotifications: boolean = false;
  private pollSub?: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router,
    protected location: Location,
    protected modalService: ModalService,
    private http: HttpClient, // Injected HttpClient
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
    this.startNotificationPolling(); // Start polling
  }

  ngOnDestroy(): void {
    this.pollSub?.unsubscribe(); // Clean up subscription
  }

  login() {
    this.router.navigate(['/login'], {queryParams: {redir: this.router.url}});
  }

  logout() {
    localStorage.removeItem("credential")
    location.reload()
  }

  // Notification methods
  startNotificationPolling(){
    this.pollSub = interval(10000).subscribe(() => {
      this.http.get<string[]>('http://localhost:8080/notifications').subscribe({
        next: (notifications) => {
          if(this.showNotifications === false){
            this.unreadCount = notifications.length;
          }
        },
        error: (err) => console.error('Notification pool failed', err)
      });
    });
  }

  toggleNotifications(){
    if (!this.showNotifications){
      this.http.get<string[]>('http://localhost:8080/notifications').subscribe({
        next: (messages) => {
          this.notifications = messages;
          this.showNotifications = true;
          this.unreadCount = 0;
        },
        error: (err) => console.error('Failed to load notifications', err)
      });
    }
    else{
      this.showNotifications = false;
      this.notifications = [];
    }
  }
}
