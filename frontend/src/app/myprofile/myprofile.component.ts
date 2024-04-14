import {Component} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";
import Swal from "sweetalert2";

@Component({
  selector: 'app-myprofile',
  templateUrl: './myprofile.component.html',
  styleUrls: ['./myprofile.component.css']
})
export class MyprofileComponent {

  display = "none";
  showPwChangeModal = "none"
  userId = localStorage.getItem("userId")

  changePwForm: FormGroup

  ngOnInit() { // runs before loading the component
    if (!this.userService.roleMatch([
      "Hallgató",
      "ADMIN",
      "Elnök",
      "Bíráló",
      "Jegyző",
      "Témavezető"
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarning()
      this.router.navigate(['/login'])
    }


  }


  constructor(private userService: UserService,
              private router: Router) {
    this.changePwForm = new FormGroup({
        password: new FormControl(null, [Validators.required]),
        //     token: new FormControl(null, [Validators.required]),
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

  alertWithWarning()
  {
    Swal.fire("Figyelem!",'Csak bejelentkezett felhasználó tekintheti meg az oldalt!','warning')
  }

  changePw() {
    console.log(localStorage.getItem("userId"))

    this.userService.changePassword_two(this.userId, this.changePwForm.value.password, this.changePwForm.value.newPassword)?.subscribe(
      (resp) => {
        console.log(resp)
        this.alertWithNewPassword()

      },
      (err) =>
        console.log(err.status)
    )
  }


  openModal() {
    this.display = "block";
    this.showPwChangeModal = "block"
  }

  onCloseHandled() {
    this.display = "none";
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
