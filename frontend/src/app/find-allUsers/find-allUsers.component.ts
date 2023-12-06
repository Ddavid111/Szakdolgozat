import { Component, OnInit } from '@angular/core';
import {findAllUsersService} from "../_services/find-allUsers.service";

@Component({
  selector: 'app-find-allUsers',
  templateUrl: './find-allUsers.component.html',
  styleUrls: ['./find-allUsers.component.css']
})
export class FindAllUsersComponent implements OnInit {


  data: any

  constructor(private findAllUsersService: findAllUsersService) {

  }

  ngOnInit() {
    this.findAllUsers()
  }

  roleMatching(roleId: number) {
    switch (roleId){
      case 0:
        return "Hallgató"
      case 1:
        return  "Elnök"
      case 2:
        return "Jegyző"
      case 3:
        return "Bíráló"
      case 4:
        return "Témavezető"
      default:
        return "Undefinied"
    }
  }


  deleteUser(user : any){
    this.findAllUsersService.deleteUser(user[0]).subscribe(
      (resp) => {
        this.findAllUsers()
        console.log(resp)
      },
      (err) => {
        alert('error van: ' + err)
      })
  }

  findAllUsers() {
    this.findAllUsersService.findAllUsers().subscribe(
      (resp) => {
        this.data = resp

        this.data.forEach((e:any) => {
          e[8] = this.roleMatching(e[8])
        })

        console.log(resp)
      },
      (err) => {
        alert('error van: ' + err)
      }
    )
  }
}
