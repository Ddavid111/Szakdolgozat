import { Component } from '@angular/core';
import {AddUsersService} from "../_services/add-users.service";
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-add-users',
  templateUrl: './add-users.component.html',
  styleUrls: ['./add-users.component.css']
})

export class AddUsersComponent {

  selectedRoleId: any

  rolesToDropDownList = [
    {
      roleId: 0,
      role: 'Hallgató',
    },
    {
      roleId: 1,
      role: 'Elnök'
    },
    {
      roleId: 2,
      role: 'Jegyző'
    },
    {
      roleId: 3,
      role: 'Bíráló'
    },
    {
      roleId: 4,
      role: 'Témavezető'
    }

  ]

  constructor(private addUserService: AddUsersService) {

  }

  addFormData(addUserForm: NgForm) {
    this.addUserService.addUser(addUserForm.value).subscribe(
      (resp) => {
        alert("Sikerült")
      },
      (err) => {
        alert('error: ' + err)
      }
    )
  }
}
