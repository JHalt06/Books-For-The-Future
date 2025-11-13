import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, catchError, firstValueFrom, Observable, of} from 'rxjs';
import {User, UserRole} from '../models/user.model';
import { Need } from '../models/need.model';
import { CupboardService } from './cupboard.service';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private basket = new BehaviorSubject<Need[]>([]);
  private url = "http://localhost:8080/users"

  jsonOnlyHeader = () => ({
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
      // possible that i will need to add a user-id header field and add to all relevent classes... 
    })
  });

  jsonOnlyHeader2 = () => ({
    headers: new HttpHeaders({
        'Content-Type': 'application/json',
    }),
    responseType: "text" as "json"
  });

  constructor(
    private http: HttpClient,
    private cupboardService: CupboardService,
    private authService: AuthService
  ) {
    authService.getCurrentUserObj().subscribe(() => this.refreshBasket())
  }

  async createUser(username:string, password:string) {
    await firstValueFrom(this.http.post<User>(this.url, {username: username, password: password}, this.jsonOnlyHeader()))
  }

  getUser(id: string): Observable<User> {
      return this.http.get<User>(`${this.url}/${id}`, this.jsonOnlyHeader())
  }

  getCount(): Observable<number> {
      return this.http.get<number>(`${this.url}/count`, this.jsonOnlyHeader2())
  }

  updateUser(user: User): Observable<User> {
      return this.http.put<User>(`${this.url}/${user.username}`, user, this.jsonOnlyHeader())
  }

  deleteUser(id: number): Observable<boolean> {
      return this.http.delete<boolean>(`${this.url}/${id}`, this.jsonOnlyHeader())
  }

  refreshBasket() {
    let user = this.authService.getCurrentUser();
    if (!user) return;
    let arr = user.basket.map(async needID => {
        return await firstValueFrom(this.cupboardService.getNeed(needID));
    })
    Promise.all(arr).then(r => this.basket.next(r));
  }

    removeNeed(id: number) {
      let newArr = this.basket.getValue().filter(v => v.id != id);
      this.basket.next(newArr);
      this.authService.getCurrentUser()!.basket = newArr.map(need => need.id);
      this.updateUser(this.authService.getCurrentUser()!)
          .pipe(
              catchError((err: any, _) => {
                  return of();
              })
          )
          .subscribe(() => {
          this.refreshBasket();
      });
    }

    getBasket() {
        return this.basket;
    }

    isManager() {
        return this.authService.getCurrentUser()?.role === UserRole.MANAGER
    }

    isHelper() {
        return this.authService.getCurrentUser()?.role === UserRole.HELPER
    }

    inBasket(basket: Need[] | null, need: Need) {
        return basket?.map(n => n.id).includes(need.id);
    }
}