import {Component, OnInit} from '@angular/core';
import {findAllUsersService} from "../_services/find-allUsers.service";
import {ListThesesesService} from "../_services/list-theseses.service";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {AddDocxService} from "../_services/add-docx.service";
import Swal from "sweetalert2";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";
@Component({
  selector: 'app-add-docx',
  templateUrl: './add-docx.component.html',
  styleUrls: ['./add-docx.component.css']
})

export class AddDocxComponent implements OnInit {

  ngOnInit() {
    if (!this.userService.roleMatch([
      "Bíráló",
      "Témavezető",
      "ADMIN"
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarningPermission()
      this.router.navigate(['/login'])
    }

    this.getStudentsToDropdown(localStorage['userId']) // It calls getThesesesToDropdown as well
    // this.getThesesesToDropdown() // Called at OnSelectStudent
  }

  students: any
  selectedStudent: any
  theses: any[] = []
  selectedTheseses: any
  reporter: any[] = []
  selectedScore: any
  addDocxForm: FormGroup


  constructor(private findAllUsersService: findAllUsersService,
              private listThesesesService: ListThesesesService,
              private addDocxService: AddDocxService,
              private router: Router,
              private userService: UserService,) {
    this.addDocxForm = new FormGroup({
      student: new FormControl(null,[Validators.required]),
      theses: new FormControl(null, [Validators.required]),
      description: new FormControl(null),
      score: new FormControl(null,[Validators.required]),
      city: new FormControl(null,[Validators.required]),
    },
      {validators: [cityError]

      })
  }

  alertWithSucces()
  {
    Swal.fire({
      title: "Sikeres",
      icon: 'success',
      allowOutsideClick: false
    }).then((result) => {
      if(!result.isDenied){
        window.location.reload()
      }
      // return result.isConfirmed;
    });
  }


  alertWithError(err: any) {
  Swal.fire("Hiba!",'Error' + err,'error')
  }

  alertWithFileDownload(err:any){
    Swal.fire("Error", 'Error downloading file' + err , 'error')
  }

  alertWithWarningPermission()
  {
    Swal.fire("Figyelem!","Nincs jogosultsága a következő odalhoz!", 'warning')
  }


  getStudentsToDropdown(userId: number) {
    // this.findAllUsersService.findUsersByRole(0).subscribe(
    this.findAllUsersService.findStudentsByLoggedInReviewer(userId).subscribe(
      (resp) => {
        console.log(resp)
        this.students = resp
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }

  onSelectStudent() {
    console.log(this.selectedStudent)
    if(this.selectedStudent !== undefined) {
      this.getThesesesToDropdown(this.selectedStudent,localStorage['userId'])
    }
  }

  getThesesesToDropdown(userId: number, reviewerId: number) {
    this.listThesesesService.findThesesByUserIdAndReviewerId(userId,reviewerId).subscribe(
      (resp: any) => {
        console.log(resp)
        this.theses = resp
      },

      (err) => {
        this.alertWithError(err.value)
      }
    )
  }


  LocalDate: String = new Date().toLocaleDateString()

  gradesToDropDownList = [
    {
      gradeId: 1,
      grade: 'Elégtelen',
    },
    {
      gradeId: 2,
      grade: 'Elégséges',
    },
    {
      gradeId: 3,
      grade: 'Közepes',
    },
    {
      gradeId: 4,
      grade: 'Jó',
    },
    {
      gradeId: 5,
      grade: 'Jeles',
    }
  ]


  addFormData(addDocxForm: FormGroup) {
    let userId = localStorage.getItem('userId')

    this.addDocxService.generateReview(addDocxForm.value, userId)
      .subscribe(response => {
        const contentDispositionHeader = response.headers.get('Content-Disposition');
        const matches = /filename=(.+)$/.exec(contentDispositionHeader as string);
        const filename = matches ? matches[1] : 'noname';
        const blob = new Blob([response.body as BlobPart], {type: 'application/octet-stream'});
        const downloadLink = document.createElement('a');
        const url = window.URL.createObjectURL(blob);

        downloadLink.href = url;
        downloadLink.download = filename;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
        window.URL.revokeObjectURL(url);
        this.alertWithSucces()
      }, error => {
        this.alertWithFileDownload(error)
      });

  }

}
export const cityError: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  let field = control.get('city')
  if (field === null || field?.value === null || field?.value === "") {
    return {
      cityError: true
    }
  }
  return null
}
