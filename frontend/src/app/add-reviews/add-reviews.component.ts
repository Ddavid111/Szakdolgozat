import { Component } from '@angular/core';
import {AddReviewsService} from "../_services/add-reviews.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-add-reviews',
  templateUrl: './add-reviews.component.html',
  styleUrls: ['./add-reviews.component.css']
})
export class AddReviewsComponent {

  constructor(private addReviewsService: AddReviewsService) {
  }


  addFormData(addReviewsForm: NgForm)
  {
    this.addReviewsService.addReviews(addReviewsForm.value).subscribe(
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
