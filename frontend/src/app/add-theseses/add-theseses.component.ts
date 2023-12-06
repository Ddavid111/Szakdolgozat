import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {AddThesesesService} from "../_services/add-theseses.service";
import {findAllUsersService} from "../_services/find-allUsers.service";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-theseses',
  templateUrl: './add-theseses.component.html',
  styleUrls: ['./add-theseses.component.css']
})
export class AddThesesesComponent implements OnInit{

  jelentkezik: boolean = false;
  students: any[] = []
  selectedStudent: any
  ngOnInit() { // runs before loading the component
    if(!this.userService.roleMatch(0)) { // if the roles are not correct, navigate to login page before the component would have loaded
      alert('Only HALLGATO role can view this page.')
      this.router.navigate(['/login'])
    }

    this.getStudentsToDropdown()
    console.log(this.students)
  }
  constructor(
              private addThesesesService: AddThesesesService,
              private findAllUsersService: findAllUsersService,
              private userService: UserService,
              private router: Router
              ) {
  }

  getStudentsToDropdown(){
    this.findAllUsersService.findAllUsers().subscribe(
      (resp) =>{
        for (let i = 0; i < Object.keys(resp).length; i++) {
          console.log(Object.values(resp)[i])
          if (Object.values(resp)[i][8] === 0) {

            this.students.push(Object.values(resp)[i])
            // console.log(Object.values(resp)[i])
          }
        }
      },
      (err) => {
        alert('error: ' + err)
      }
    )
  }

  addFormData(addThesesesForm: NgForm) {
    this.addThesesesService.addTheseses(addThesesesForm.value).subscribe(

      (resp) => {
        console.log(addThesesesForm.value)
        alert("Sikeresen adatbázisba írva!")
        //másik lapra navigálás
        this.router.navigate(['/uploadPDFZIP'])

      },
      (err) => {
        alert('error: ' + err)
        console.log(addThesesesForm.value)
      }
    )
  }


}
