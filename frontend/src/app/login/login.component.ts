import {Component} from '@angular/core';
import {UserService} from "../_services/user.service";
import {AuthService} from "../_services/auth.service";
import {Router} from "@angular/router";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  NgForm,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import Swal from "sweetalert2";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})




export class LoginComponent {
  display = "none";
  showPwChangeModal = "none"

  forgottenPwForm = {
    username: ''
  }

  changePwForm: FormGroup

  constructor(private userService: UserService, private authService: AuthService, private router: Router) {
    this.changePwForm = new FormGroup({
        username: new FormControl(null, [Validators.required]),
        token: new FormControl(null, [Validators.required]),
        newPassword: new FormControl(null, [Validators.required]),
        confirmPassword: new FormControl(null, [Validators.required])
      },
      {
        validators: matchPwValidator
      })
  }

  alertWithNewPassword()
  {
    Swal.fire("Köszönjük!",'Jelszó sikeresen megváltoztatva. Mostantól az új jelszóval lehet bejelentkezni','success')
  }

  alertWithUnauth() {
    Swal.fire("Hiba", 'Unauth 401',  'error');
  }

  alertWithWrongPassword() {
    Swal.fire("Hiba", 'Wrong pw 500',  'error');
  }


  login(loginForm: NgForm) {
    this.userService.login(loginForm.value).subscribe(
      (response: any) => {
        console.log('loginform: ' + loginForm.value.userName)
        this.authService.setRoles(response.user.roleId)
        localStorage.setItem('userId', response.user.userId)
        console.log("loginnál user role id: " + response.user.roleId)
        this.authService.setToken(response.jwtToken)

        if (response.user.id === 0) {
          this.router.navigate(['/listTheseses']); // ide rak miután a login sikeres és ha roleid 0
        } else {
          this.router.navigate(['/findAllUsers']); // ide rak miután a login sikeres
        }


      },

      (error) => {
        if (error.status === 401) {
          this.alertWithUnauth()
        }

        if (error.status === 500) {
          this.alertWithWrongPassword()
        }
        console.log(error)
      }
    )

  }

  forgottenPw() {
    console.log("forgotten pw button")
    console.log("username: " + this.forgottenPwForm.username)

    this.userService.sendForgottenPasswordEmail(this.forgottenPwForm.username).subscribe(
      (resp) => {
        console.log("email sent")
        console.log(resp)
      },
      (err) =>
        console.log(err.status)
    )
  }

  changePw() {
    console.log(this.changePwForm.value.username)

    this.userService.changePassword(this.changePwForm.value.username, this.changePwForm.value.token, this.changePwForm.value.newPassword).subscribe(
      (resp) => {
        console.log("pw changed successfully")
        console.log(resp)
        this.alertWithNewPassword()
      },
      (err) =>
        console.log(err.status)
    )
  }


  openModal() {
    this.display = "block";
  }

  onCloseHandled() {
    this.display = "none";
  }

  showNewPwModal() {
    this.display = "none"
    this.showPwChangeModal = "block"


  }

  closePwChangeModal() {
    this.showPwChangeModal = "none"
  }

}

export const matchPwValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {

  let password = control.get('newPassword');
  let confirmPw = control.get('confirmPassword')

  if ((password && confirmPw) && password?.value != confirmPw?.value) {
    return {
      matchPwValidator: true
    }
  }
  return null;
}
