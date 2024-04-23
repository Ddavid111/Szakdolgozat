import {Component} from '@angular/core';
import {AddUsersService} from "../_services/add-users.service";
import Swal from 'sweetalert2';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-users',
  templateUrl: './add-users.component.html',
  styleUrls: ['./add-users.component.css']
})

export class AddUsersComponent {

  selectedRole: any
  isVisiblepositionField: any
  isDropdownList: any
  addUserForm: FormGroup

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


  constructor(private addUserService: AddUsersService,
              private router: Router,
              private userService: UserService) {
    this.addUserForm = new FormGroup({
        title: new FormControl(null),
        birthday: new FormControl(null,),
        email: new FormControl(null, [Validators.required]),
        neptunCode: new FormControl(null),
        username: new FormControl(null, [Validators.required]),
        fullname: new FormControl(null, [Validators.required]),
        password: new FormControl(null, [Validators.required]),
        birthPlace: new FormControl(null),
        mothersMaidenName: new FormControl(null),
        workplace: new FormControl(null),
        pedigreeNumber: new FormControl(null),
        role: new FormControl(null, [Validators.required]),
        position: new FormControl( null)
      },
      {
        validators: [birthdayError, emailError, neptunCodeError, usernameError, fullnameError,
          passwordError, birthPlaceError,
          mothersMaidenNameError, workplaceError, pedigreeNumberError, roleError, positionError]
      })
  }

  ngOnInit(): void {
    if (!this.userService.roleMatch([
      "ADMIN"
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarning()
      this.router.navigate(['/login'])
    }
  }

  alertWithSucces()
  {
    Swal.fire("Köszönjük!",'Felhasználó sikeresen tárolva','success')
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }

  alertWithWarning()
  {
    Swal.fire("Figyelem!",'Only HALLGATO role can view this page.','warning')
  }


  onRoleChange() {
    console.log(this.selectedRole)
    this.isVisiblepositionField = false
    this.isDropdownList = false
    if (this.selectedRole === "Hallgató")
    {
      this.isVisiblepositionField = false;

    }
    else if (this.selectedRole === "Elnök" || this.selectedRole === "Jegyző" || this.selectedRole === "Témavezető")
    {
      this.isVisiblepositionField = false
      this.isDropdownList = true;
    }
    else if(this.selectedRole === "Bíráló")
    {
      this.isVisiblepositionField = true
    }
  }


  addFormData(addUserForm: FormGroup) {
    this.addUserService.addUser(addUserForm.value).subscribe(
      (resp) => {
        this.alertWithSucces()
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }

}

export const birthdayError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let birthday = control.get('birthday')
  let selectedRole = control.get('role')?.value;
  if (selectedRole === "Hallgató" && (birthday === null || birthday?.value === null || birthday?.value === "")) {
    return {
      birthdayError: true
    }
  }
  return null
}

export const emailError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let email = control.get('email')
  if (email === null || email?.value === null || email?.value === "") {
    return {
      emailError: true
    }
  }

  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!emailRegex.test(email.value)) {
    return { invalidEmail: true };
  }

  return null
}

export const neptunCodeError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let neptunCode = control.get('neptunCode')
  let selectedRoleId = control.get('role')?.value;
  if (selectedRoleId === "Hallgató" && (neptunCode === null || neptunCode?.value === null || neptunCode?.value === "")) {
    return {
      neptunCodeError: true
    }
  }
  return null
}

export const usernameError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let username = control.get('username')
  if (username === null || username?.value === null || username?.value === "") {
    return {
      usernameError: true
    }
  }
  return null
}

export const fullnameError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let fullname = control.get('fullname')
  if (fullname === null || fullname?.value === null || fullname?.value === "") {
    return {
      fullnameError: true
    }
  }
  return null
}

export const passwordError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let password = control.get('password')
  if (password === null || password?.value === null || password?.value === "") {
    return {
      passwordError: true
    }
  }
  return null
}

export const birthPlaceError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let birthPlace = control.get('birthPlace')
  let selectedRoleId = control.get('role')?.value;
  if (selectedRoleId === "Hallgató" && (birthPlace === null || birthPlace?.value === null || birthPlace?.value === "")) {
    return {
      birthPlaceError: true
    }
  }
  return null
}

export const mothersMaidenNameError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let mothersMaidenName = control.get('mothersMaidenName')
  let selectedRoleId = control.get('role')?.value;
    if (selectedRoleId === "Hallgató" && (mothersMaidenName === null || mothersMaidenName?.value === null || mothersMaidenName?.value === "")) {
      return {
        mothersMaidenNameError: true
      }
    }
    return null
}

export const workplaceError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let workplace = control.get('workplace')
  let selectedRoleId = control.get('role')?.value;
  if ((selectedRoleId === "Elnök" || selectedRoleId === "Jegyző" || selectedRoleId === "Bíráló" || selectedRoleId === "Témavezető") && (workplace === null || workplace?.value === null || workplace?.value === "")) {
    return {
      workplaceError: true
    }
  }
  return null
}

export const pedigreeNumberError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let pedigreeNumber = control.get('pedigreeNumber')
  let selectedRoleId = control.get('role')?.value;
  if (selectedRoleId === "Hallgató" && (pedigreeNumber === null || pedigreeNumber?.value === null || pedigreeNumber?.value === "")) {
    return {
      pedigreeNumberError: true
    }
  }
  return null
}

export const roleError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let role = control.get('role')
  if (role === null || role?.value === null || role?.value === "") {
    return {
      roleError: true
    }
  }
  return null
}

export const positionError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let position = control.get('position')
  let selectedRoleId = control.get('role')?.value;
  if (selectedRoleId !== "Hallgató")
  {
    if (position === null || position?.value === null || position?.value === "") {
      return {
        positionError: true
      }
    }
  }
  return null
}
