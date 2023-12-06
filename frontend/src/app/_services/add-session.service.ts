import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddSessionService {

  constructor(private http: HttpClient) { }

  API = 'http://localhost:8080'


  public addSession(sessionData: any) {
    return this.http.post(this.API + '/addSession', sessionData)
  }

}
