import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddUsersService {


  constructor(private http: HttpClient) { }

  API = 'http://localhost:8080'

  public addUser(userData: any) {
    return this.http.post(this.API + '/addUser', userData)
  }
}
