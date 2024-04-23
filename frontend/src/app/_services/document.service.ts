import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class DocumentService {

  constructor(private http: HttpClient) {
  }

  API = "http://localhost:8080"

  public generateDocx() {
    return this.http.get(this.API + '/generateDocx')
  }

}
