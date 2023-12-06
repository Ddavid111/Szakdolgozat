import { Component } from '@angular/core';
import {AddFilesService} from "../_services/add-files.service";

@Component({
  selector: 'app-upload-pdf-zipfiles',
  templateUrl: './upload-pdf-zipfiles.component.html',
  styleUrls: ['./upload-pdf-zipfiles.component.css']
})
export class UploadPDFZIPFilesComponent {
  selectedFile1: File | null = null
  selectedFile2: File | null = null

  constructor(private addFilesService: AddFilesService) {

  }

  onFileSelected(event: any, buttonNumber: number) {
    const file = event.target.files[0]

    switch (buttonNumber) {
      case 1: this.selectedFile1 = file; break
      case 2: this.selectedFile2 = file; break
      default: alert('Error van.'); break
    }
  }

  uploadFile(buttonNumber: number) {
    const uploadData = new FormData();
    switch (buttonNumber) {
      case 1: {
        if (!this.selectedFile1) {
          alert('No file selected (1)');
          return;
        }
        uploadData.append('file', this.selectedFile1, this.selectedFile1.name);
        break
      }
      case 2: {
        if (!this.selectedFile2) {
          alert('No file selected (2)');
          return;
        }
        uploadData.append('file', this.selectedFile2, this.selectedFile2.name);
        break
      }
      default: alert('Error van.'); break
    }



    this.addFilesService.addFiles(uploadData)
      .subscribe(res => {
        console.log(res);
        alert('Sikeresen adatbázisba írva.')
      }, err => {
        alert(err);
        console.error(err);
      });
  }

}
