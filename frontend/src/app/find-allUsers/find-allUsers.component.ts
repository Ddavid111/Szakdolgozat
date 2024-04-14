import {Component, OnInit} from '@angular/core';
import {findAllUsersService} from "../_services/find-allUsers.service";
import Swal from "sweetalert2";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-find-allUsers',
  templateUrl: './find-allUsers.component.html',
  styleUrls: ['./find-allUsers.component.css']
})
export class FindAllUsersComponent implements OnInit {

  data: any
  isVisiblepositionField: any
  isDropdownList: any
  temp = null as any
  student: any
  neptunCode: any
  email: any
  workplace: any

  rolesToDropDownList = [
    {
      role: 'Hallgató',
    },
    {
      role: 'Elnök'
    },
    {
      role: 'Jegyző'
    },
    {
      role: 'Bíráló'
    },
    {
      role: 'Témavezető'
    },
    {
      role: 'ADMIN'
    }

  ]

  positionsToDropDownList = [
    {
      position: 'egyetemi tanársegéd',
    },
    {
      position: 'egyetemi adjunktus',
    },
    {
      position: 'egyetemi docens',
    },
    {
      position: 'egyetemi tanár',
    },
  ];

  sort(key: any) {
    console.log(this.key)
    this.key = key;
    this.reverse = !this.reverse;
    console.log(this.data)
  }


  usersForUpdate = {
    id: '',
    title: '',
    email: '',
    username: '',
    fullname: '',
    neptunCode: '',
    workplace: '',
    position: '',
    role: '',
  }

  constructor(private findAllUsersService: findAllUsersService,
              private router: Router,
              private userService: UserService,) {

  }

  ngOnInit() {
      if (!this.userService.roleMatch([
      "ADMIN"
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarning()
      this.router.navigate(['/login'])
    }

    this.findAllUsers()
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }

  alertWithWarning()
  {
    Swal.fire("Figyelem!",'Only HALLGATO role can view this page.','warning')
  }

  updateUsers() {
    console.log(this.usersForUpdate)
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
    this.usersForUpdate.id = user.id
    this.usersForUpdate.title = user.title
    this.usersForUpdate.email = user.email
    this.usersForUpdate.username = user.username
    this.usersForUpdate.fullname = user.fullname
    this.usersForUpdate.neptunCode = user.neptunCode
    this.usersForUpdate.workplace = user.workplace
    this.usersForUpdate.position = user.position
    this.usersForUpdate.role = user.role
  }

  key: any = 0;
  reverse: boolean = false;

  p: number = 1

  searchForStudent() {
    if (this.student == "") {
      this.ngOnInit();
    } else {
      console.log(this.data)
      this.data = this.temp.filter((res: any) => {
        return res.fullname.toLocaleLowerCase().match(this.student.toLocaleLowerCase())
      })
    }
  }

  searchForNeptunCode() {
    if (this.neptunCode == "") {
      this.ngOnInit();
    } else {
      console.log(this.data)
      this.data = this.temp.filter((res: any) => {
        return res.neptunCode.toLocaleLowerCase().match(this.neptunCode.toLocaleLowerCase())
      })
    }
  }

  searchForEmail() {
    if (this.email == "") {
      this.ngOnInit();
    } else {
      console.log(this.data)
      this.data = this.temp.filter((res: any) => {
        return res.email.toLocaleLowerCase().match(this.email.toLocaleLowerCase())
      })
    }
  }

  searchForWorkplace() {
    if (this.workplace == "") {
      this.ngOnInit();
    } else {
      console.log(this.data)
      this.data = this.temp.filter((res: any) => {
        return res.workplace.toLocaleLowerCase().match(this.workplace.toLocaleLowerCase())
      })
    }
  }

  onRoleChange() {
    console.log(this.usersForUpdate.role)
    this.isVisiblepositionField = false;
    this.isDropdownList = false
    if (this.usersForUpdate.role === "Hallgató") //hallgató
    {
      this.isVisiblepositionField = false;

    } else if (this.usersForUpdate.role === "Elnök" || this.usersForUpdate.role === "Jegyző"|| this.usersForUpdate.role === "ADMIN"|| this.usersForUpdate.role === "Témavezető") {
      this.isVisiblepositionField = false
      this.isDropdownList = true;
    } else
      this.isVisiblepositionField = true
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
    this.findAllUsersService.findAllUsersToDisplay().subscribe(
      (resp : any) => {
        console.log(resp)
        this.data = resp
        this.temp = this.data
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }
}
