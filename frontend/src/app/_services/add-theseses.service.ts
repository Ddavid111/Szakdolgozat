import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddThesesesService {

  constructor(private http: HttpClient) {
  }

  API = 'http://localhost:8080'

  public addTheseses(thesesesData: any) {
    return this.http.post(this.API + '/addTheseses', thesesesData)
  }
}
