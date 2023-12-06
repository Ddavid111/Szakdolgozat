import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class findAllUsersService {

  constructor(private http: HttpClient) { }

  API = "http://localhost:8080"

  public findAllUsers() {
    return this.http.get(this.API + '/findAllUsers')
  }

  public deleteUser(id : any){
    return this.http.delete(this.API + '/deleteUser?id='+id)
  }

  public findUserById(id: any) {
    return this.http.get(this.API + '/findUserById?id='+ id)
  }
}
