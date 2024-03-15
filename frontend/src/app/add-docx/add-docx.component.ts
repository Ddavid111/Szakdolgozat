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
@Component({
  selector: 'app-add-docx',
  templateUrl: './add-docx.component.html',
  styleUrls: ['./add-docx.component.css']
})

export class AddDocxComponent implements OnInit {

  ngOnInit() {
    this.getStudentsToDropdown() // It calls getThesesesToDropdown as well
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
              private addDocxService: AddDocxService) {
    this.addDocxForm = new FormGroup({
      student: new FormControl(null,[Validators.required]),
      theses: new FormControl(null, [Validators.required]),
      description: new FormControl(null),
      score: new FormControl(null,[Validators.required]),
      city: new FormControl(null,[Validators.required]),
      // invitationDate: new FormControl(null,[Validators.required]),
      invitationAcceptionDate: new FormControl(null,[Validators.required]),
      responseDate: new FormControl(null,[Validators.required]),
      submissionDate: new FormControl(null,[Validators.required]),
    },
      {validators: [cityError]

      })
  }

  alertWithSucces()
  {
    Swal.fire("Köszönjük!",'Felhasználó sikeresen tárolva','success')
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }

  alertWithFileDownload(err:any){
    Swal.fire("Error", 'Error downloading file' + err , 'error')
  }

  getStudentsToDropdown() {
    this.findAllUsersService.findUsersByRole(0).subscribe(
      (resp) => {
        this.students = resp
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }

  onSelectStudent() {
    this.theses
    this.getThesesesToDropdown()
    console.log(this.selectedStudent)
  }

  getThesesesToDropdown() {
    // TODO: SQL-ben userId-ra filterezni thesis-t, aztán akkor nem itt kell szarakodni
    this.listThesesesService.getThesesList().subscribe(
      (resp) => {
        console.log(resp)
        let allThesis = resp as any;
        this.theses = allThesis.filter((thesis: any) => thesis.userId == this.selectedStudent)
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
