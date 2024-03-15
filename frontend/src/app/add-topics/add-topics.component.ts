import {Component} from '@angular/core';
import {AddTopicsService} from "../_services/add-topics.service";
import {NgForm} from "@angular/forms";
import Swal from "sweetalert2";

@Component({
  selector: 'app-add-topics',
  templateUrl: './add-topics.component.html',
  styleUrls: ['./add-topics.component.css']
})
export class AddTopicsComponent {

  constructor(private addTopicsService: AddTopicsService) {
  }

  alertWithSucces()
  {
    Swal.fire("Köszönjük!",'Szakdolgozat sikeresen tárolva','success')
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }

  addFormData(addTopicsForm: NgForm) {

    this.addTopicsService.addTopics(addTopicsForm.value).subscribe(
      (resp) => {
        console.log(addTopicsForm.value)
        this.alertWithSucces()
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }


}
