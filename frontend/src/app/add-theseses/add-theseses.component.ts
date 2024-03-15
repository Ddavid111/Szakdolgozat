import {Component, OnInit} from '@angular/core';
import Swal from 'sweetalert2';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {AddThesesesService} from "../_services/add-theseses.service";
import {findAllUsersService} from "../_services/find-allUsers.service";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-theseses',
  templateUrl: './add-theseses.component.html',
  styleUrls: ['./add-theseses.component.css']
})
export class AddThesesesComponent implements OnInit {

  jelentkezik: boolean = false;
  selectedSupervisor: any
  selectedConsultant: any
  supervisors: any
  consultants: any
  students: any
  selectedStudent: any
  addThesesForm: FormGroup

  alertWithSucces()
  {
    Swal.fire("Köszönjük!",'Szakdolgozat sikeresen tárolva','success')
  }

  alertWithWarning()
  {
    Swal.fire("Figyelem!",'Only HALLGATO role can view this page.','warning')
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }

  ngOnInit() { // runs before loading the component
    if (!this.userService.roleMatch(0)) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarning()
      this.router.navigate(['/login'])
    }
    this.getSupervisorToDropdown()
    this.getStudentsToDropdown()

  }


  constructor(
    private addThesesesService: AddThesesesService,
    private findAllUsersService: findAllUsersService,
    private userService: UserService,
    private router: Router
  ) {
    this.addThesesForm = new FormGroup({
        title: new FormControl(null, [Validators.required]),
        faculty: new FormControl(null, [Validators.required]),
        department: new FormControl(null, [Validators.required]),
        speciality: new FormControl(null, [Validators.required]),
        language: new FormControl(null, [Validators.required]),
        hasMscApply: new FormControl(null),
        submissionDate: new FormControl(null, [Validators.required]),
        answer: new FormControl(null),
        defenseScore: new FormControl(null),
        subjectScore: new FormControl(null),
        finalScore: new FormControl(null),
        userId: new FormControl(null, [Validators.required]),
        supervisorId: new FormControl(null, [Validators.required]),
        consultantId: new FormControl(null)
      },
      {
        validators: [titleError, facultyError, departmentError, specialityError, languageError,
          submissionDateError, userIdError, supervisorIdError]

      })

  }


  getSupervisorToDropdown() {
    this.findAllUsersService.findUsersByRole(4).subscribe(
      (resp) => {
        this.supervisors = resp
        this.consultants = resp
      }
    )
  }

  getStudentsToDropdown() {
    this.findAllUsersService.findUsersByRole(0).subscribe(
      (resp) => {
        this.students = resp
      }
    )
  }

  addFormData() {
    console.log(this.addThesesForm)

    this.addThesesesService.addTheseses(this.addThesesForm.value).subscribe(
      (resp) => {
        console.log(this.addThesesForm.value)
        this.alertWithSucces()
        //másik lapra navigálás
        this.router.navigate(['/uploadPDFZIP'])

      },
      (err) => {
        this.alertWithError(err.value)
        console.log(this.addThesesForm.value)
      }
    )
  }


}

export const titleError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let title = control.get('title')
  if (title === null || title?.value === null || title?.value === "") {
    return {
      titleError: true
    }
  }
  return null
}

export const facultyError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let faculty = control.get('faculty')
  if (faculty === null || faculty?.value === null || faculty?.value === "") {
    return {
      facultyError: true
    }
  }
  return null
}

export const departmentError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let department = control.get('department')
  if (department === null || department?.value === null || department?.value === "") {
    return {
      departmentError: true
    }
  }
  return null
}

export const specialityError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let speciality = control.get('speciality')
  if (speciality === null || speciality?.value === null || speciality?.value === "") {
    return {
      specialityError: true
    }
  }
  return null
}

export const languageError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('language')
  if (field === null || field?.value === null || field?.value === "") {
    return {
      languageError: true
    }
  }
  return null
}

export const submissionDateError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('submissionDate')
  if (field === null || field?.value === null || field?.value === "") {
    return {
      submissionDateError: true
    }
  }
  return null
}

export const userIdError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('userId')
  if (field === null || field?.value === null || field?.value === "") {
    return {
      userIdError: true
    }
  }
  return null
}

export const supervisorIdError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('supervisorId')
  if (field === null || field?.value === null || field?.value === "") {
    return {
      supervisorIdError: true
    }
  }
  return null
}
