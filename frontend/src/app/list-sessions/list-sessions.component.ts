import { Component } from '@angular/core';
import {AddSessionService} from "../_services/add-session.service";
import {FindAllUsersComponent} from "../find-allUsers/find-allUsers.component";
import {findAllUsersService} from "../_services/find-allUsers.service";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";
import Swal from "sweetalert2";

@Component({
  selector: 'app-list-sessions',
  templateUrl: './list-sessions.component.html',
  styleUrls: ['./list-sessions.component.css']
})
export class ListSessionsComponent {

  data: any
  constructor(private addSessionService: AddSessionService,
              private router: Router,
              private userService: UserService) {
  }

  ngOnInit() {
    if (!this.userService.roleMatch([
      "Elnök",
      "Jegyző",
      "ADMIN"
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarning()
      this.router.navigate(['/login'])
    }

    this.listSessions()

  }

  alertWithWarning()
  {
    Swal.fire("Figyelem!","Nincs jogosultsága a következő odalhoz!", 'warning')
  }

  //OrderBy and pagination
  key: any = 0;
  reverse: boolean = false;

  p: number = 1

  listSessions() {
    this.addSessionService.getSessionsListToDisplay().subscribe(
      (resp:any) => {
        console.log(resp)

        resp.forEach((resp: any) => {

          let dateObj = new Date(resp.date) // submissionDate field from the DB
          let monthInCorrectForm = (dateObj.getMonth() + 1).toString()
          if (monthInCorrectForm.length == 1) {
            monthInCorrectForm = '0' + monthInCorrectForm
          }

          let dayInCorrectForm = dateObj.getDate().toString()
          if (dayInCorrectForm.toString().length == 1) {
            dayInCorrectForm = "0" + dayInCorrectForm
          }
          resp.date = dateObj.getFullYear() + '.' + monthInCorrectForm + '.' + dayInCorrectForm + '.'
        })


      this.data = resp

      }
    )

  }








}
