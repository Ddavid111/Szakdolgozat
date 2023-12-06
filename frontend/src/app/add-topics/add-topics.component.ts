import { Component } from '@angular/core';
import {AddTopicsService} from "../_services/add-topics.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-add-topics',
  templateUrl: './add-topics.component.html',
  styleUrls: ['./add-topics.component.css']
})
export class AddTopicsComponent {


  constructor(private addTopicsService: AddTopicsService) {
  }


  addFormData(addTopicsForm: NgForm) {

    this.addTopicsService.addTopics(addTopicsForm.value).subscribe(
      (resp) => {
        alert("Sikeresen adatbázisba írva!")
        //   alert(resp)
      },
      (err) => {
        alert("vmi elromlott")
        alert(err.value)
      }
    )
  }




}
