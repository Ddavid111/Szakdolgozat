import {Component, OnInit} from '@angular/core';
import {ListThesesesService} from "../_services/list-theseses.service";
import {findAllUsersService} from "../_services/find-allUsers.service";
import {NgForm} from "@angular/forms";
import {AddTopicsService} from "../_services/add-topics.service";

@Component({
  selector: 'app-list-theseses',
  templateUrl: './list-theseses.component.html',
  styleUrls: ['./list-theseses.component.css'],

})
export class ListThesesesComponent implements OnInit {
  data = null as any;
  temp = null as any // copy of data for correct searching
  jelentkezik: boolean = false;
  title: any
  student: any
  user: any

  studentlist: any
  supervisorlist: any
  consultantlist: any

  thesesForUpdate = {
    id: '',
    userId: '',
    title: '',
    faculty: '',
    department: '',
    speciality: '',
    language: '',
    hasMscApply: '',
    submissionDate: '',
    answer: '',
    topicScore: '',
    defenseScore: '',
    subjectScore: '',
    finalScore: '',
    supervisorId: '',
    consultantId: ''
  }

  selectedTopicId: any
  selectedGrade: any

  thesisToDisplay = {
    title: "",
    topicName: "",
    id: ""
  }

  topicsToDropdownList: any

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

  constructor(private listThesesesService: ListThesesesService,
              private FindAllUsersService: findAllUsersService) {
    this.getThesesList()
  }

  ngOnInit() {
    this.getThesesList()
  }

  // If user clicks to "Új tétel", this method invoked first
  addTopic(thesis: any) {
    this.thesisToDisplay.title = thesis.title
    this.thesisToDisplay.id = thesis.id

    // Get topic list to the dropdown
    this.listThesesesService.getTopicList().subscribe(
      (RTopicList) => {
        console.log(RTopicList)
        this.topicsToDropdownList = RTopicList
      },
      (ErrTopicList) => {
        alert("Error when getting topic list: " + ErrTopicList.value)
      }
    )
  }

  //searching
  searchForTitle() {
    if (this.title == "") {
      this.ngOnInit();
    } else {
      this.data = this.temp.filter((res: any) => {
        return res.title.toLocaleLowerCase().match(this.title.toLocaleLowerCase())
      })
    }
  }

  searchForStudent() {
    if (this.student == "") {
      this.ngOnInit();
    } else {
      this.data = this.temp.filter((res: any) => {
        return res.user.fullname.toLocaleLowerCase().match(this.student.toLocaleLowerCase())
      })
    }
  }

  // If user selects a topic and a grade and then clicks to "OK" button at #chooseTopicModal, this method invoked.
  chooseTopic() {
    this.data.selectedTopicName = this.selectedTopicId // Tételszámot tartalmaz mindkettő. A this.data-s azért kell, hogy meg tudja jeleníteni, a másik pedig a DB-be írásért.
    this.listThesesesService.setTopicForStudent(this.selectedTopicId, this.thesisToDisplay.id, this.selectedGrade).subscribe(
      (resp) => {
        this.getThesesList()
      },
      (err) => {
        alert("Error during set topic for student: " + err.value)
      }
    )
  }

  updateTheses() {
    this.listThesesesService.updateTheses(this.thesesForUpdate).subscribe(
      (resp) => {
        this.getThesesList()
      },
      (err) => {
        alert('error van: ' + err.value)
      }
    )
  }

  edit(theses: any) {
    this.getStudents()
    this.thesesForUpdate.id = theses.id
    this.getSupervisors()
    this.thesesForUpdate.supervisorId = theses.supervisorId
    this.getConsultants()
    this.thesesForUpdate.consultantId = theses.consultantId
    this.thesesForUpdate.userId = theses.userId
    this.thesesForUpdate.title = theses.title
    this.thesesForUpdate.faculty = theses.faculty
    this.thesesForUpdate.department = theses.department
    this.thesesForUpdate.speciality = theses.speciality
    this.thesesForUpdate.language = theses.language
    this.thesesForUpdate.hasMscApply = theses.hasMscApply

    let dateInDbFormat = theses.submissionDate
    let [yyyy, mm, dd] = dateInDbFormat.toString().split('.')
    this.thesesForUpdate.submissionDate = yyyy + "-" + mm + "-" + dd // bemegy a modal-ba

    this.thesesForUpdate.answer = theses.answer
    this.thesesForUpdate.topicScore = theses.topicScore
    this.thesesForUpdate.defenseScore = theses.defenseScore
    this.thesesForUpdate.subjectScore = theses.subjectScore
    this.thesesForUpdate.finalScore = theses.finalScore
  }

  deleteTheses(theses: any) {
    this.listThesesesService.deleteTheses(theses.id).subscribe(
      (resp) => {
        this.getThesesList()
        console.log(resp)
      },
      (err) => {
        alert('error van: ' + err.value)
      })
  }

  // Get the content of the Theses table
  getThesesList() {
    //this.listThesesesService.getThesesList().subscribe(
    this.listThesesesService.getThesesListToDisplay().subscribe(
      (resp: any) => {


        console.log(resp)


        // Convert date format per thesis from the DB style to fancy style to display
        resp.forEach((resp: any) => {
          let dateObj = new Date(resp.submissionDate) // submissionDate field from the DB
          let monthInCorrectForm = (dateObj.getMonth() + 1).toString()
          if (monthInCorrectForm.length == 1) {
            monthInCorrectForm = '0' + monthInCorrectForm
          }

          let dayInCorrectForm = dateObj.getDate().toString()
          if (dayInCorrectForm.toString().length == 1) {
            dayInCorrectForm = "0" + dayInCorrectForm
          }
          resp.submissionDate = dateObj.getFullYear() + '.' + monthInCorrectForm + '.' + dayInCorrectForm + '.'
        })


        this.data = resp
        this.temp = this.data // copy of data for correct searching
      },
      (err) => {
        alert(err)
      }
    )
  }


  // sorting
  key: any = 0;
  reverse: boolean = false;

  sort(key: any) {
    this.key = key;
    this.reverse = !this.reverse;
    console.log(this.data)
  }

  // Pagination
  p: number = 1


  getStudents() {
    this.FindAllUsersService.findUsersByRole(0).subscribe(
      (resp) => {
        this.studentlist = resp
        console.log(this.studentlist)
      },
      (err) => {
        console.log("error when get studentdata to dropdown list: " + err.value)
      })
  }

  getSupervisors() {
    this.FindAllUsersService.findUsersByRole(4).subscribe(
      (resp) => {
        this.supervisorlist = resp
      },
      (err) => {
        console.log("error when get supervisordata to dropdown list: " + err.value)
      })
  }

  getConsultants() {
    this.FindAllUsersService.findUsersByRole(4).subscribe(
      (resp) => {
        this.consultantlist = resp
      },
      (err) => {
        console.log("error when get consultant data to dropdown list: " + err.value)
      })
  }
}
