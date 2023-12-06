import {Component, OnInit} from '@angular/core';
import {findAllUsersService} from "../_services/find-allUsers.service";
import {ListThesesesService} from "../_services/list-theseses.service";
import {NgForm} from "@angular/forms";
import {AddDocxService} from "../_services/add-docx.service";

@Component({
  selector: 'app-add-docx',
  templateUrl: './add-docx.component.html',
  styleUrls: ['./add-docx.component.css']
})
export class AddDocxComponent implements OnInit{

  selectedGrade : any
  students: any[] = []
  selectedStudent: any
  theses: any[] =[]
  selectedTheseses: any


  ngOnInit() {
    this.getThesesesToDropdown()
    this.getStudentsToDropdown()

  }

  constructor(private findAllUsersService: findAllUsersService,
              private listThesesesService: ListThesesesService,
              private addDocxService: AddDocxService) {
  }

  getStudentsToDropdown(){
    this.findAllUsersService.findAllUsers().subscribe(
      (resp) => {
        for (let i = 0; i < Object.keys(resp).length; i++) {
          console.log(Object.values(resp)[i])
          if (Object.values(resp)[i][8] === 0) {
            this.students.push(Object.values(resp)[i])
          }
        }
      },
      (err) => {
        alert(err.value)
      }
    )
  }


  getThesesesToDropdown(){

    this.listThesesesService.getThesesList().subscribe(
      (resp)=>{
        for (let i = 0; i < Object.keys(resp).length; i++) {

          // Object.values(resp)[i][16]: the user_id in the Thesis table
          // this.students[i][0]: the id of the User table
          if (Object.values(resp)[i][16] === this.students[i][0]) {
            this.theses.push(Object.values(resp)[i])
          }
        }
      },
      (err) => {
        alert(err.value)
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

  addFormData (addDocxForm: NgForm){
    this.addDocxService.addDocx(addDocxForm.value).subscribe(
      (resp) => {
        alert("Sikeresen wordbe írva!")
        //   alert(resp)
      },
      (err) => {
        alert("vmi elromlott")
        alert(err.value)
      }
    )

  }


}
