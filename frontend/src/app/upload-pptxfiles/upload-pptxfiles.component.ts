import { Component } from '@angular/core';
import { AddFilesService } from "../_services/add-files.service";
import { DocumentService } from "../_services/document.service";

@Component({
  selector: 'app-upload-pptxfiles',
  templateUrl: './upload-pptxfiles.component.html',
  styleUrls: ['./upload-pptxfiles.component.css']
})
export class UploadPPTXFilesComponent {
  selectedFile: File | null | undefined;
  constructor(private addFilesService: AddFilesService, private documentService: DocumentService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadFile() {
    if (!this.selectedFile) {
      console.error('No file selected.');
      return;
    }

    const uploadData = new FormData();
    uploadData.append('file', this.selectedFile, this.selectedFile.name);

    this.addFilesService.addFiles(uploadData)
      .subscribe(res => {
        console.log(res);
        alert('Sikeresen adatbázisba írva.');
      }, err => {
        alert(err);
        console.error(err);
      });
  }

  generateDocx() {
    this.documentService.generateDocx().subscribe(
      (resp) => {
        console.log(resp)
      },
      (err) => {
        alert('error van: ' + err.value)
      }
    )
  }

}
