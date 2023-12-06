import {Component, OnInit} from '@angular/core';
import {AddSessionService} from "../_services/add-session.service";
import {NgForm} from "@angular/forms";
import {findAllUsersService} from "../_services/find-allUsers.service";

@Component({
  selector: 'app-add-session',
  templateUrl: './add-session.component.html',
  styleUrls: ['./add-session.component.css']
})
export class AddSessionComponent implements OnInit{



  students: any[] = []
  selectedStudent: any
  selectedMember: any
  members: any[] = []


  constructor(private addSessionService: AddSessionService,
              private findAllUsersService: findAllUsersService
  ) {  }

  ngOnInit() {
    this.getStudentsToDropdown()
    this.getMembersToDropdown()
  }

  getMembersToDropdown() {
    this.findAllUsersService.findAllUsers().subscribe(
      (resp) =>{
        for (let i = 0; i < Object.keys(resp).length; i++) {
         // console.log(Object.values(resp)[i])
          switch (Object.values(resp)[i][8]) {
            case 1: this.members.push(Object.values(resp)[i]); break; // president
            case 2: this.members.push(Object.values(resp)[i]); break; // notary
            case 6: this.members.push(Object.values(resp)[i]); break; // other member
          //  default: alert("Error filling the dropdown list of members.");
          }
        }
      },
      (err) => {
        alert(err.value)
      }
    )
  }


  getStudentsToDropdown(){
    this.findAllUsersService.findAllUsers().subscribe(
      (resp) =>{
        for (let i = 0; i < Object.keys(resp).length; i++) {
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

  addFormData(addSessionForm: NgForm)
  {
    this.addSessionService.addSession(addSessionForm.value).subscribe(
      (resp)=>{
      alert("Sikeresen adatbázisba írva!")
      },
      (err) => {
        alert("vmi elromlott")
        alert(err.value)
      }
    )
  }




}
