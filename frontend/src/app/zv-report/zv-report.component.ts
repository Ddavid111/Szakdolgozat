import {Component, OnInit} from '@angular/core';
import {findAllUsersService} from "../_services/find-allUsers.service";
import {AddSessionService} from "../_services/add-session.service";
import {ListThesesesService} from "../_services/list-theseses.service";
import {AddDocxService} from "../_services/add-docx.service";
import {FormControl, FormGroup, NgForm, Validators} from "@angular/forms";
import Swal from "sweetalert2";

@Component({
  selector: 'app-zv-report',
  templateUrl: './zv-report.component.html',
  styleUrls: ['./zv-report.component.css']
})
export class ZvReportComponent implements OnInit {

  students: any
  selectedStudent: any
  birthplace: any[] = []
  selectedBirthplace: any
  theses: any[] = []
  selectedTheseses: any
  session: any[] = []
  selectedSession: any
  selectedQuestion: any
  selectedGrade: any
  selectedThesesGrade: any
  addDocxForm: FormGroup


  ngOnInit() {
    this.getStudentsToDropdown() // It calls getThesesesToDropdown as well
    //this.getThesesesToDropdown() // Called in getStudentsToDropdown because we need to wait its response.
    // this.getCodeToDropDown()
  }

  constructor(private findAllUsersService: findAllUsersService,
              private addSessionService: AddSessionService,
              private listThesesesService: ListThesesesService,
              private addDocxService: AddDocxService) {

    this.addDocxForm = new FormGroup({
      student: new FormControl(null,[Validators.required]),
      theses: new FormControl(null,[Validators.required]),
      session: new FormControl(null,[Validators.required]),
      othermembers: new FormControl(null),
      question: new FormControl(null,[Validators.required]),
      tgradeId: new FormControl(null,[Validators.required]),
      gradeId: new FormControl(null,[Validators.required]),
      description: new FormControl(null),
    })
  }

  alertWithSucces()
  {
    Swal.fire("Köszönjük!",'Felhasználó sikeresen tárolva','success')
  }


  questionToDropDownList = [
    {
      question: 'kielégítő',
    },
    {
      question: 'nem kielégítő',
    }
  ]

  gradesToDropDownList = [
    {
      gradeId: 1,
    },
    {
      gradeId: 2,
    },
    {
      gradeId: 3,
    },
    {
      gradeId: 4,
    },
    {
      gradeId: 5,
    }

  ]

  tgradesToDropDownList = [
    {
      tgradeId: 1,
    },
    {
      tgradeId: 2,
    },
    {
      tgradeId: 3,
    },
    {
      tgradeId: 4,
    },
    {
      tgradeId: 5,
    }

  ]


  getStudentsToDropdown() {
    this.findAllUsersService.findUsersByRole(0).subscribe(
      (resp) => {
        this.students = resp
      },
    )
  }

  onSelectStudent() {
    this.getThesesesToDropdown()
    this.getCodeToDropDown()
    console.log(this.selectedStudent)
  }

  getThesesesToDropdown() {
    this.listThesesesService.getThesesList().subscribe(
      (resp) => {
        console.log(resp)
        let allThesis = resp as any;
        this.theses = allThesis.filter((thesis: any) =>
          thesis.userId == this.selectedStudent)
        console.log(this.theses)
      },
    )
  }

  getCodeToDropDown() {
    this.addSessionService.getSessionList().subscribe(
      (resp) => {
        console.log(resp)

        let allSession = resp as any[]
        console.log(allSession)
        console.log(this.selectedStudent)

        this.session = allSession.filter((sessions: any) => {
          console.log(sessions.students.id)
          return sessions.students[0].id == this.selectedStudent
        });

        console.log(this.session)
      }
    );
  }



  addFormData(addDocxForm: FormGroup) {
    let userId = localStorage.getItem('userId')
    this.addDocxService.addDocx(addDocxForm.value, userId).subscribe(
      (resp) => {
        console.log(resp)

        const contentDispositionHeader = resp.headers.get('Content-Disposition');
        const matches = /filename=(.+)$/.exec(contentDispositionHeader as string);
        const filename = matches ? matches[1] : 'noname';
        const blob = new Blob([resp.body as BlobPart], {type: 'application/octet-stream'});
        const downloadLink = document.createElement('a');
        const url = window.URL.createObjectURL(blob);

        downloadLink.href = url;
        downloadLink.download = filename;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
        window.URL.revokeObjectURL(url);


        this.alertWithSucces()
      })

  }



}
