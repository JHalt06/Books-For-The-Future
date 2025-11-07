import {Need} from '../models/need.model';
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class CupboardService {
  jsonOnlyHeader = () => ({
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
      // possible that i will need to add a user-id header field and add to all relevent classes...
    })
  });

  constructor(
    private httpClient: HttpClient,
  ) {}

  getNeeds(): Observable<Need[]> {
    return this.httpClient.get<Need[]>('http://localhost:8080/cupboard/needs', this.jsonOnlyHeader())
  }

  deleteNeed(id: number): Observable<boolean> {
    return this.httpClient.delete<boolean>(`http://localhost:8080/cupboard/needs/${id}`, this.jsonOnlyHeader())
  }

  searchNeeds(name: String): Observable<Need[]> {
    return this.httpClient.get<Need[]>(`http://localhost:8080/cupboard?name=${name}`, this.jsonOnlyHeader())
  }

  getNeed(id: number): Observable<Need> {
    return this.httpClient.get<Need>(`http://localhost:8080/cupboard/need/${id}`, this.jsonOnlyHeader())
  }

  updateNeed(id: number, data: Need): Observable<boolean> {
    return this.httpClient.put<boolean>(`http://localhost:8080/cupboard/needs/${id}`, data, this.jsonOnlyHeader())
  }

  createNeed(need: Need): Observable<Need> {
    return this.httpClient.post<Need>('http://localhost:8080/cupboard/need', need, this.jsonOnlyHeader())
  }

  checkoutNeed(data: {needID: number, quantity: number}[]) {
    return this.httpClient.put(`http://localhost:8080/cupboard/checkout`, data, this.jsonOnlyHeader())
  }
}
