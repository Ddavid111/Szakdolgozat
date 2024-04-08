import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {AuthService} from "./auth.service";
import Swal from "sweetalert2";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClientlient: HttpClient, private authService: AuthService) {
  }

  API = 'http://localhost:8080/';

  requestHeader = new HttpHeaders(
    {
      "No-Auth": "True"
    }
  );

  alertWithError() {
    Swal.fire("Hiba", 'UserId is null',  'error');
  }

  public login(loginData: any) {
    return this.httpClientlient.post(this.API + 'authenticate', loginData, {headers: this.requestHeader})
  }

  public roleMatch(allowedRoles: any): boolean {
    let isMatch = false;
    const userRoles: any = this.authService.getRoles();
    // console.log('allowed roles' + allowedRoles)
    // console.log("current userRole:")
    // console.log(userRoles)

    allowedRoles.forEach((role: any)=>{
      if (userRoles === role) {
        isMatch = true
      }
    })



    // if(userRoles != null && userRoles) {
    //   for(let i = 0; i < userRoles.length; i++) {
    //     for(let j = 0; j < allowedRoles.length; j++) {
    //       if(userRoles[i].roleName === allowedRoles[j]) {
    //         isMatch = true
    //         return isMatch;
    //       } else {
    //         return isMatch;
    //       }
    //     }
    //   }
    // }
    return isMatch;
  }

  /* public forUser() {
     return this.httpclient.get(this.API + 'forUser', {responseType: 'text'})
   }

   */


  public sendForgottenPasswordEmail(username: string) {
    let httpParams = new HttpParams().append("username", username)
    return this.httpClientlient.post(this.API + 'sendForgottenPasswordEmail', httpParams)
  }

  public sendForgottenPasswordEmail_two(password: string) {
    let httpParams = new HttpParams().append("password", password)
    return this.httpClientlient.post(this.API + 'sendForgottenPasswordEmail_two', httpParams)
  }


  // changePassword (If forgotten)
  public changePassword(username: string, token: string, newPassword: string) {
    let httpParams = new HttpParams()
      .append("username", username)
      .append("token", token)
      .append("newPassword", newPassword)
    return this.httpClientlient.post(this.API + 'changePassword', httpParams)
  }

  // changePassword (If the user wants)
  public changePassword_two(userId: any, oldPassword: string, newPassword: string) {
    if (userId != null) {
      let httpParams = new HttpParams()
        .append("userId", userId)
        .append("oldPassword", oldPassword)
        .append("newPassword", newPassword)
      return this.httpClientlient.post(this.API + 'changePassword_two', httpParams)
    } else {
      this.alertWithError()
      return null
    }
  }

}
