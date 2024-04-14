import {Component, OnInit} from '@angular/core';
import {AddSessionService} from "../_services/add-session.service";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {findAllUsersService} from "../_services/find-allUsers.service";
import Swal from "sweetalert2";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-session',
  templateUrl: './add-session.component.html',
  styleUrls: ['./add-session.component.css']
})
export class AddSessionComponent implements OnInit {

  studentsToDropdown: any
  students : any
  membersToDropdown: any
  members: any
  chairmanId: any
  secretaryId: any
  addSessionForm: FormGroup

  constructor(private addSessionService: AddSessionService,
              private findAllUsersService: findAllUsersService,
              private router: Router,
              private userService: UserService,
  ) {
    this.addSessionForm = new FormGroup({
        location: new FormControl(null, [Validators.required]),
        date: new FormControl(null, [Validators.required]),
        startHour: new FormControl(null, [Validators.required]),
        endHour: new FormControl(null, [Validators.required]),
        students: new FormControl(null, [Validators.required]),
        members: new FormControl(null,[Validators.required]),
        chairman: new FormControl(null,[Validators.required]),
        secretary: new FormControl(null,[Validators.required]),
        description: new FormControl(null),
        code: new FormControl(null, [Validators.required])
      },
      {
        validators: [locationError, dateError, startHourError, startHourValueError, endHourError, endHourValueError, endHourrError,
          studentsError, membersError, chairmanError, secretaryError, codeError]
      }
    )

  }
  alertWithSucces()
  {
    Swal.fire("Köszönjük!",'Záróvizsga sikeresen tárolva','success')
  }

  alertWithWarning() {
    Swal.fire({
      title: "Türelem",
      html: "Kérem várjon amíg elküldjük az emaileket!",
      icon: 'warning',
      showConfirmButton: false,
      allowOutsideClick: false
    })
  }

  alertWithWarningPermission()
  {
    Swal.fire("Figyelem!","Nincs jogosultsága a következő odalhoz!", 'warning')
  }


  alertWithError(err: any) {
    Swal.fire("Hiba", 'Error:' + err,  'error');
  }

  ngOnInit() {
    if (!this.userService.roleMatch([
      "Elnök",
      "Jegyző",
      "ADMIN"
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarningPermission()
      this.router.navigate(['/login'])
    }

    this.getStudentsToDropdown()
    this.getMembersToDropdown()
  }

  getMembersToDropdown() {
    let roleIds = ["Elnök","Jegyző","Bíráló","Témavezető","ADMIN"] // roleIds of members
    this.findAllUsersService.findUsersByRoleList(roleIds).subscribe(
      (resp) => {
        console.log(resp)
        this.membersToDropdown = resp
      }
    )
  }


  getStudentsToDropdown() {
    this.findAllUsersService.findUsersByRoleList(["Hallgató"]).subscribe(
      (resp) => {
        this.studentsToDropdown = resp
      }
    )
  }

  addFormData() {

    console.log(this.addSessionForm)
    this.addSessionForm.value.students = [] as any
    this.addSessionForm.value.members = [] as any
    this.addSessionForm.value.chairman = {id: this.chairmanId}
    this.addSessionForm.value.secretary = {id: this.secretaryId}


    this.students.forEach((userId: any) =>
     {
       this.addSessionForm.value.students.push({
         id: userId
       })
    })

    this.members.forEach((userId: any) =>
    {
      this.addSessionForm.value.members.push({
        id: userId
      })
    })

    this.addSessionService.addSession(this.addSessionForm.value).subscribe(
      (resp) => {
        console.log(Object.values(resp))
        this.alertWithSucces()
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }

  waiting()
  {

    this.alertWithWarning()

  }


}

export const locationError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let location = control.get('location')
  if (location === null || location?.value === null || location?.value === "") {
    return {
      locationError: true
    }
  }
  return null
}

export const dateError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let date = control.get('date')
  if (date === null || date?.value === null || date?.value === "") {
    return {
      dateError: true
    }
  }
  return null
}

export const startHourError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('startHour')
  if (field === null || field?.value === null || field?.value === "") {
    return {
      startHourError: true
    }
  }
  return null

}
export const startHourValueError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('startHour')
  if (Number.parseInt(field?.value) < 6 || Number.parseInt(field?.value) > 20) {
    return {
      startHourValueError: true
    }
  }
  return null

}

export const endHourError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('endHour')
  if (field === null || field?.value === null || field?.value === "") {
    return {
      endHourError: true
    }
  }
  return null

}

export const endHourValueError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('endHour')
  if (Number.parseInt(field?.value) < 8 || Number.parseInt(field?.value) > 22) {
    return {
      endHourValueError: true
    }
  }
  return null

}

export const endHourrError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let endHour = control.get('endHour')
  let startHour = control.get('startHour')
  if (Number.parseInt(startHour?.value) >= Number.parseInt(endHour?.value)) {
    return {
      endHourrError: true
    }
  }
  return null
}

export const studentsError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let students = control.get('students')
  if (students === null || students?.value === null || students?.value === "") {
    return {
      studentsError: true
    }
  }
  return null
}

export const membersError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let members = control.get('members')
  if (members === null || members?.value === null || members?.value === "") {
    return {
      membersError: true
    }
  }
  return null
}

export const chairmanError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let chairman = control.get('chairman')
  if (chairman === null || chairman?.value === null || chairman?.value === "") {
    return {
      chairmanError: true
    }
  }
  return null
}

export const secretaryError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let secretary = control.get('secretary')
  if (secretary === null || secretary?.value === null || secretary?.value === "") {
    return {
      secretaryError: true
    }
  }
  return null
}

export const codeError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let code = control.get('code')
  if (code === null || code?.value === null || code?.value === "") {
    return {
      codeError: true
    }
  }
  return null
}
