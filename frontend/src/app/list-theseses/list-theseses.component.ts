import { Component, OnInit } from '@angular/core';
import {ListThesesesService} from "../_services/list-theseses.service";
import {AddUsersComponent} from "../add-users/add-users.component";
import {AddUsersService} from "../_services/add-users.service";
import {findAllUsersService} from "../_services/find-allUsers.service";
import {FindAllUsersComponent} from "../find-allUsers/find-allUsers.component";

@Component({
  selector: 'app-list-theseses',
  templateUrl: './list-theseses.component.html',
  styleUrls: ['./list-theseses.component.css'],

})
export class ListThesesesComponent implements OnInit {
  data= null as any;
  jelentkezik: boolean = false;
  title: any
  student: any

  thesesForUpdate= {
    id: '',
    title: '',
    faculty: '',
    department: '',
    speciality: '',
    language: '',
    hasMscApply: '',
    submissionDate: new Date,
    answer: '',
    topicScore: '',
    defenseScore: '',
    subjectScore: '',
    finalScore: ''
  }

  selectedTopicId: any

  thesisToDisplay = {
    title: "",
    topicName: ""
  }


  topicsToDropdownList = [
    {
      id: 0,
      topic: 'OOP alapelvei',
    },
    {
      id: 1,
      topic: 'Kivételkezelés C#-ban'
    }
  ]

  constructor(private listThesesesService: ListThesesesService,
              private FindAllUsersService: findAllUsersService) {
    this.getThesesList()
  }
  display = "none";
  openModal() {
    this.display = "block";
  }

  addTopic(thesis: any) {
    this.display = "block";
    this.thesisToDisplay = thesis

  }

  //searching
  searchForTitle() {
    if(this.title == "") {
      this.ngOnInit();
    } else {
      this.data= this.data.filter((res:any) =>{
        return res[14].toLocaleLowerCase().match(this.title.toLocaleLowerCase())
      })
    }
  }

  searchForStudent() {
    if(this.student == "") {
      this.ngOnInit();}
    else {
      this.data= this.data.filter((res:any) =>{
        return res.studentname.toLocaleLowerCase().match(this.student.toLocaleLowerCase())
      })
    }
  }

  onCloseHandled() {
    this.display = "none";
  }

  chooseTopic() {
    console.log(this.selectedTopicId)

    this.data.selectedTopicName = this.selectedTopicId
  }

  ngOnInit() {
    this.getThesesList()
  }


  updateTheses(){
    this.listThesesesService.updateTheses(this.thesesForUpdate).subscribe(
      (resp)=>{
        this.getThesesList()
        this.onCloseHandled()
      },
      (err)=>{
        alert('error van: ' + err.value)
      }
    )
  }


  edit(theses: any){
    this.thesesForUpdate.id = theses[0]
    // student name?
    this.thesesForUpdate.title = theses[14]
    this.thesesForUpdate.faculty = theses[4]
    this.thesesForUpdate.department = theses[3]
    this.thesesForUpdate.speciality = theses[8]
    this.thesesForUpdate.language = theses[7]
    this.thesesForUpdate.hasMscApply = theses[6]
    this.thesesForUpdate.submissionDate = theses[10]
    //console.log(document.getElementById("submissionDate")?.getAttribute("ng-reflect-model"))
   // console.log("date default value: " + this.thesesForUpdate.submissionDate.setDate(2020))
    //this.thesesForUpdate.submissionDate = new Date("2023-11-10") // ezt nem viszi be a modal-ba!

    let d = this.thesesForUpdate.submissionDate// ok
    console.log("date at edit: " + d)
    let [yyyy, mm, dd] = d.toString().split('.')
    let x = new Date(Number(yyyy), Number(mm) - 1, Number(dd))
    console.log("date after convert: " + x)
   // console.log(Date.parse(d))



    this.thesesForUpdate.submissionDate = d
    this.thesesForUpdate.answer = theses[1]
    this.thesesForUpdate.topicScore = theses[15]
    this.thesesForUpdate.defenseScore = theses[2]
    this.thesesForUpdate.subjectScore = theses[9]
    this.thesesForUpdate.finalScore = theses[5]

    this.openModal()

  }

  deleteTheses(theses : any){
    this.listThesesesService.deleteTheses(theses[0]).subscribe(
      (resp) => {
        this.getThesesList()
        console.log(resp)
      },
      (err) => {
        alert('error van: ' + err.value)
      })
  }


  getThesesList() {
    this.listThesesesService.getThesesList().subscribe(
      (resp) => {
        for(let i = 0; i < Object.keys(resp).length; i++) {

          let dateObj = new Date(Object.values(resp)[i][10])
          let monthInCorrectForm =(dateObj.getMonth()+1).toString()
          if(monthInCorrectForm.length == 1) {
            monthInCorrectForm = '0' + monthInCorrectForm
          }

          let dayInCorrectForm = dateObj.getDate().toString()
          if(dayInCorrectForm.toString().length == 1) {
            dayInCorrectForm = "0" + dayInCorrectForm
          }
          let dateYYYYMMDD = dateObj.getFullYear() + '.' + monthInCorrectForm + '.' + dayInCorrectForm + '.'

          if(Object.values(resp)[i][16] != null) {

            this.FindAllUsersService.findUserById(Object.values(resp)[i][16]).subscribe(
              (resp1) => {
                for (let j = 0; j < Object.keys(resp1).length; j++) {
                  this.data[i].studentname= Object.values(resp1)[j][5]
                }

                this.data[i][10] = dateYYYYMMDD

              },
              (error) =>
              {
                alert(error.status)
              }
            )
            //   console.log("student: " + Object.keys(this.student))
          }


        }

        this.data = resp

        console.log(resp)

      },
      (err) => {
        alert(err)
      }
    )
  }

  getStudentById(id: any) {
    console.log("ID before call to backend: " + id)
    this.FindAllUsersService.findUserById(id).subscribe(
      (resp) => {
        this.student = resp
      },
      (error) =>
      {
        alert(error.status)
      }
    )
  }

  key: any='data';
  reverse: boolean = false;
  sort(key: any) {
    this.key = key;
    this.reverse = !this.reverse;
  }

  p: number = 1


}
