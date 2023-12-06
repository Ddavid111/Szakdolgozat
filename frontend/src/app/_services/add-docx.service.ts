import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddDocxService {

  constructor(private http: HttpClient) { }

  API = 'http://localhost:8080'

  public addDocx(docxData: any) {
    return this.http.post(this.API + '/generateDocx', docxData)
  }
}
