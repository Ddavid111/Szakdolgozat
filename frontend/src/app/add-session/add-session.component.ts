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
  presidentId: any
  notaryId: any
  addSessionForm: FormGroup

  constructor(private addSessionService: AddSessionService,
              private findAllUsersService: findAllUsersService
  ) {
    this.addSessionForm = new FormGroup({
        location: new FormControl(null, [Validators.required]),
        date: new FormControl(null, [Validators.required]),
        startHour: new FormControl(null, [Validators.required]),
        endHour: new FormControl(null, [Validators.required]),
        students: new FormControl(null, [Validators.required]),
        members: new FormControl(null,[Validators.required]),
        president: new FormControl(null,[Validators.required]),
        notary: new FormControl(null,[Validators.required]),
        description: new FormControl(null),
        code: new FormControl(null, [Validators.required])
      },
      {
        validators: [locationError, dateError, startHourError, startHourValueError, endHourError, endHourValueError, endHourrError,
          studentsError, membersError, presidentError, notaryError, codeError]
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


  alertWithError(err: any) {
    Swal.fire("Hiba", 'Error:' + err,  'error');
  }

  ngOnInit() {
    this.getStudentsToDropdown()
    this.getMembersToDropdown()
  }

  getMembersToDropdown() {
    let roleIds = [1, 2, 3, 4] // roleIds of members
    this.findAllUsersService.findUsersByRoleList(roleIds).subscribe(
      (resp) => {
        this.membersToDropdown = resp
      }
    )
  }


  getStudentsToDropdown() {
    this.findAllUsersService.findUsersByRole(0).subscribe(
      (resp) => {
        this.studentsToDropdown = resp
      }
    )
  }

  addFormData() {


    this.addSessionForm.value.students = [] as any
    this.addSessionForm.value.members = [] as any
    this.addSessionForm.value.president = {userId: this.presidentId}
    this.addSessionForm.value.notary = {userId: this.notaryId}


    this.students.forEach((userId: any) =>
     {
       this.addSessionForm.value.students.push({
         userId: userId
       })
    })

    this.members.forEach((userId: any) =>
    {
      this.addSessionForm.value.members.push({
        userId: userId
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

export const presidentError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let president = control.get('president')
  if (president === null || president?.value === null || president?.value === "") {
    return {
      presidentError: true
    }
  }
  return null
}

export const notaryError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let notary = control.get('notary')
  if (notary === null || notary?.value === null || notary?.value === "") {
    return {
      notaryError: true
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
