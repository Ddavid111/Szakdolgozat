import {Component, OnInit} from '@angular/core';
import {findAllUsersService} from "../_services/find-allUsers.service";
import Swal from "sweetalert2";

@Component({
  selector: 'app-find-allUsers',
  templateUrl: './find-allUsers.component.html',
  styleUrls: ['./find-allUsers.component.css']
})
export class FindAllUsersComponent implements OnInit {

  data: any
  selectedRoleId: any
  isVisiblePostField: any
  isDropdownList: any

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

  postsToDropDownList = [
    {
      post: 'egyetemi tanársegéd',
    },
    {
      post: 'egyetemi adjunktus',
    },
    {
      post: 'egyetemi docens',
    },
    {
      post: 'egyetemi tanár',
    },
  ];


  usersForUpdate = {
    userId: '',
    title: '',
    // birthday: '',
    email: '',
    username: '',
    fullname: '',
    neptunCode: '',
    // mothersMaidenName: '',
    // birthPlace: '',
    workplace: '',
    // pedigreeNumber: '',
    post: '',
    roleId: '',
  }

  constructor(private findAllUsersService: findAllUsersService) {

  }

  ngOnInit() {
    this.findAllUsers()
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }

  roleMatching(roleId: number) {
    switch (roleId) {
      case 0:
        return "Hallgató"
      case 1:
        return "Elnök"
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

  reverseRoleMatching(roleName: string) {
    switch (roleName) {
      case "Elnök":
        return 1
      case "Hallgató":
        return 0
      case "Jegyző":
        return 2
      case "Témavezető":
        return 4
      case "Bíráló":
        return 3

      default:
        return 9999
    }
  }

  updateUsers() {
    this.findAllUsersService.updateUsers(this.usersForUpdate).subscribe(
      (resp) => {
        this.findAllUsers()
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }

  edit(user: any) {
    this.usersForUpdate.userId = user.userId
    this.usersForUpdate.title = user.title

    // let dateInDbFormat = user.birthday
    // let [yyyy, mm, dd] = dateInDbFormat.toString().split('.')
    // this.usersForUpdate.birthday = yyyy + "-" + mm + "-" + dd



    this.usersForUpdate.email = user.email
    this.usersForUpdate.username = user.username
    this.usersForUpdate.fullname = user.fullname
    this.usersForUpdate.neptunCode = user.neptunCode
    // this.usersForUpdate.mothersMaidenName = user.mothersMaidenName
    // this.usersForUpdate.birthPlace = user.birthPlace
    this.usersForUpdate.workplace = user.workplace
    // this.usersForUpdate.pedigreeNumber = user.pedigreeNumber
    this.usersForUpdate.post = user.post
    this.usersForUpdate.roleId = user.roleId  //this.reverseRoleMatching(user.roleId).toString()


  }

  key: any = 0;
  reverse: boolean = false;

  p: number = 1

  onRoleChange() {
    console.log(this.usersForUpdate.roleId)
    this.selectedRoleId = parseInt(this.usersForUpdate.roleId)
    console.log(this.selectedRoleId)
    this.isVisiblePostField = false;
    this.isDropdownList = false
    if (this.selectedRoleId === 0) //hallgató
    {
      this.isVisiblePostField = false;

    } else if (this.selectedRoleId === 1 || this.selectedRoleId === 2 || this.selectedRoleId === 4) {
      this.isVisiblePostField = false
      this.isDropdownList = true;
    } else
      this.isVisiblePostField = true
  }

  deleteUser(user: any) {
    this.findAllUsersService.deleteUser(user.userId).subscribe(
      (resp) => {
        this.findAllUsers()
        console.log(resp)
      },
      (err) => {
        this.alertWithError(err.value)
      })
  }

  findAllUsers() {
    // this.findAllUsersService.findAllUsers().subscribe(
    this.findAllUsersService.findAllUsersToDisplay().subscribe(
      (resp : any) => {

        console.log(resp)

        // resp.forEach((resp: any) => {
        //   let dateObj = new Date(resp.birthday) // submissionDate field from the DB
        //   let monthInCorrectForm = (dateObj.getMonth() + 1).toString()
        //   if (monthInCorrectForm.length == 1) {
        //     monthInCorrectForm = '0' + monthInCorrectForm
        //   }
        //
        //   let dayInCorrectForm = dateObj.getDate().toString()
        //   if (dayInCorrectForm.toString().length == 1) {
        //     dayInCorrectForm = "0" + dayInCorrectForm
        //   }
        //   resp.birthday = dateObj.getFullYear() + '.' + monthInCorrectForm + '.' + dayInCorrectForm + '.'
        // })

        this.data = resp
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }
}
