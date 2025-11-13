import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Need } from "../models/need.model";

@Injectable({
  providedIn: 'root'
})
export class NeedsService {
  private readonly baseUrl = '/api';

  constructor(private http: HttpClient) {}

  // For helpers - get all available needs
  getCupboardNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.baseUrl}/cupboard/needs`);
  }

  searchNeeds(query: string): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.baseUrl}/needs?q=${query}`);
  }

  addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(`${this.baseUrl}/cupboard/need`, need);
  }

  updateNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(`${this.baseUrl}/cupboard/need/update`, need);
  }
  
  getAllNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.baseUrl}/cupboard/needs`);
  }

  deleteNeed(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/cupboard/need`, { 
      body: { id } 
    });
  }
}