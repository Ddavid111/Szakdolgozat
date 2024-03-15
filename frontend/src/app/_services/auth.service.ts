import {Injectable} from '@angular/core';
import Swal from "sweetalert2";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() {
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", 'Error:' + err,  'error');
  }

  public setRoles(roles: []) {
    localStorage.setItem("roles", JSON.stringify(roles));
  }

  public getRoles(): [] {
    const roleString = localStorage.getItem('roles');

    if (roleString) {
      try {
        return JSON.parse(roleString)
      } catch (e) {
        this.alertWithError(e)
        return []
      }

    } else {
      return [];
    }
  }

  public setToken(jwtToken: string) {
    localStorage.setItem('jwtToken', jwtToken);
  }

  public getToken()//: string {
  {
    return localStorage.getItem('jwtToken');
  }

  public getUserId() {
    return localStorage.getItem('userId')

  }


  public clear() {
    localStorage.clear();
  }

  public isLoggedIn() {
    return this.getRoles() && this.getToken();
  }

}
