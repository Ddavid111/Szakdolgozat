import {Component} from '@angular/core';
import {AddUsersService} from "../_services/add-users.service";
import Swal from 'sweetalert2';
import {
  AbstractControl,
  FormControl,
  FormGroup, isFormGroup,
  NgForm,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';

@Component({
  selector: 'app-add-users',
  templateUrl: './add-users.component.html',
  styleUrls: ['./add-users.component.css']
})

export class AddUsersComponent {

  selectedRoleId: any
  isVisiblePostField: any
  isDropdownList: any
  addUserForm: FormGroup

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


  constructor(private addUserService: AddUsersService) {
    this.addUserForm = new FormGroup({
        title: new FormControl(null),
        birthday: new FormControl(null),
        email: new FormControl(null, [Validators.required]),
        neptunCode: new FormControl(null),
        username: new FormControl(null, [Validators.required]),
        fullname: new FormControl(null, [Validators.required]),
        password: new FormControl(null, [Validators.required]),
        birthPlace: new FormControl(null),
        mothersMaidenName: new FormControl(null),
        workplace: new FormControl(null),
        pedigreeNumber: new FormControl(null),
        roleId: new FormControl(null, [Validators.required]),
        post: new FormControl( null,[Validators.required])
      },
      {
        validators: [birthdayError, emailError, neptunCodeError, usernameError, fullnameError,
          passwordError, birthPlaceError,
          mothersMaidenNameError, workplaceError, pedigreeNumberError, roleIdError, postError]
      })
  }

  alertWithSucces()
  {
    Swal.fire("Köszönjük!",'Felhasználó sikeresen tárolva','success')
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }


  onRoleChange() {
    console.log(this.selectedRoleId)
    this.isVisiblePostField = false
    this.isDropdownList = false
    if (this.selectedRoleId === 0) //hallgató
    {
      this.isVisiblePostField = false;

    }
    else if (this.selectedRoleId === 1 || this.selectedRoleId === 2 || this.selectedRoleId === 4)
    {
      this.isVisiblePostField = false
      this.isDropdownList = true;
    }
    else if(this.selectedRoleId === 3)
    {
      this.isVisiblePostField = true
    }
  }


  addFormData(addUserForm: FormGroup) {
    if (addUserForm.value.roleId === 0) {
      addUserForm.value.post = "hallgató"
    }
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
  let selectedRoleId = control.get('roleId')?.value;
  if (selectedRoleId === 0 && (birthday === null || birthday?.value === null || birthday?.value === "")) {
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
  return null
}

export const neptunCodeError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let neptunCode = control.get('neptunCode')
  let selectedRoleId = control.get('roleId')?.value;
  if (selectedRoleId === 0 && (neptunCode === null || neptunCode?.value === null || neptunCode?.value === "")) {
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
  let selectedRoleId = control.get('roleId')?.value;
  if (selectedRoleId === 0 && (birthPlace === null || birthPlace?.value === null || birthPlace?.value === "")) {
    return {
      birthPlaceError: true
    }
  }
  return null
}

export const mothersMaidenNameError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let mothersMaidenName = control.get('mothersMaidenName')
  let selectedRoleId = control.get('roleId')?.value;
    if (selectedRoleId === 0 && (mothersMaidenName === null || mothersMaidenName?.value === null || mothersMaidenName?.value === "")) {
      return {
        mothersMaidenNameError: true
      }
    }
    return null
}
//selectedRoleId === 1 || selectedRoleId === 2 || selectedRoleId === 3 || selectedRoleId === 4
export const workplaceError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let workplace = control.get('workplace')
  let selectedRoleId = control.get('roleId')?.value;
  if ((selectedRoleId === 1 || selectedRoleId === 2 || selectedRoleId === 3 || selectedRoleId === 4) && (workplace === null || workplace?.value === null || workplace?.value === "")) {
    return {
      workplaceError: true
    }
  }
  return null
}

export const pedigreeNumberError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let pedigreeNumber = control.get('pedigreeNumber')
  let selectedRoleId = control.get('roleId')?.value;
  if (selectedRoleId === 0 && (pedigreeNumber === null || pedigreeNumber?.value === null || pedigreeNumber?.value === "")) {
    return {
      pedigreeNumberError: true
    }
  }
  return null
}

export const roleIdError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let roleId = control.get('roleId')
  if (roleId === null || roleId?.value === null || roleId?.value === "") {
    return {
      roleIdError: true
    }
  }
  return null
}

export const postError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let post = control.get('post')
  let selectedRoleId = control.get('roleId')?.value;
  if (selectedRoleId !== 0)
  {
    if (post === null || post?.value === null || post?.value === "") {
      return {
        postError: true
      }
    }
  }
  return null
}
