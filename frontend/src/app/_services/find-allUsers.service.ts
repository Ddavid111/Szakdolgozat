import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class findAllUsersService {

  constructor(private http: HttpClient) {
  }

  API = "http://localhost:8080"

  public findAllUsers() {
    return this.http.get(this.API + '/findAllUsers')
  }

  public updateUsers(users: any) {
    return this.http.put(this.API + '/updateUsers', users)
  }

  public deleteUser(id: any) {
    return this.http.delete(this.API + '/deleteUser?id=' + id)
  }

  public findUserById(id: any) {
    return this.http.get(this.API + '/findUserById?id=' + id)
  }

  public findStudentsByLoggedInReviewer(id: number)
  {
    return this.http.get(this.API + '/findStudentsByLoggedInReviewer?userId=' + id)
  }

  public findUsersByRole(roleId: number) {
    return this.http.get(this.API + '/findUsersByRole?roleId=' + roleId)
  }

  public findUsersByRoleList(roleIds: any) {
    return this.http.get(this.API + '/findUsersByRoleList?roleIds=' + roleIds)
  }

  public findAllUsersToDisplay(){
    return this.http.get(this.API + '/findAllUsersToDisplay')
  }

}
