import { Injectable, Injector } from "@angular/core";
import { User } from "../models/user.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { BehaviorSubject, firstValueFrom } from "rxjs";
import { UsersService } from "./users.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser : BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);

  private userUrl = "http://localhost:8080/login"

  constructor(
    private http: HttpClient,
    private injector: Injector
  ) {}

  async login(username: string, password: string) {
    let res = this.http.post<User>(`${this.userUrl}`, {'username': username, 'password': password},
      {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    })
    let u = await firstValueFrom(res);
    this.currentUser.next(u);
  }

  async restoreLogin(username: string) {
        const userService = this.injector.get(UsersService);
        this.currentUser.next(await firstValueFrom(userService.getUser(username)))
    }

  getCurrentUserObj() {
    console.log("currentuser:", this.currentUser)
    return this.currentUser;
  }

  getCurrentUser() {
      return this.currentUser.getValue()
  }
}