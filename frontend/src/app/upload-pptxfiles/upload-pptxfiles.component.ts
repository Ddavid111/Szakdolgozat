import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms'; // Importáljuk a NgForm-ot a Template-driven form validációhoz
import { AddFilesService } from "../_services/add-files.service";
import { DocumentService } from "../_services/document.service";
import { HttpEventType } from "@angular/common/http";
import { ListThesesesService } from "../_services/list-theseses.service";
import Swal from "sweetalert2";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-upload-pptxfiles',
  templateUrl: './upload-pptxfiles.component.html',
  styleUrls: ['./upload-pptxfiles.component.css']
})
export class UploadPPTXFilesComponent implements OnInit {
  @ViewChild('fileInput') fileInput: any;

  selectedFile: File | null | undefined;
  uploadProgress: number = 0;
  message: string | undefined;
  theses: any
  thesesId: number | undefined

  constructor(private addFilesService: AddFilesService, private documentService: DocumentService,
              private findThesesService: ListThesesesService,
              private router: Router,
              private userService: UserService) {
  }

  alertWithWarningFileType()
  {
    Swal.fire("Warning",'Invalid file type. Only pptx and ppt files are allowed..','warning')
  }

  alertWithWarningFileSize()
  {
    Swal.fire("Warning",'File size exceeds the limit of 24 MB.','warning')
  }

  alertWithWarningSelect()
  {
    Swal.fire("Warning",'Please select thesis and file.','warning')
  }

  alertWithError()
  {
    Swal.fire("Hiba",'Nem sikerült a fájl feltöltés.','error')
  }

  alertWithErrorMessage(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }

  alertWithSuccess()
  {
    Swal.fire("Siker",'Sikerült a fájl feltöltés.','success')
  }

  alertWithWarning()
  {
    Swal.fire("Figyelem!",'Only HALLGATO role can view this page.','warning')
  }

  ngOnInit(): void {
    if (!this.userService.roleMatch([
      "Hallgató",
      "ADMIN"
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarning()
      this.router.navigate(['/login'])
    }

    this.getTheses(localStorage["userId"]);
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      const extension = file.name.split('.').pop()?.toLowerCase();
      if (extension !== 'pptx' && extension !== 'ppt') {
        this.alertWithWarningFileType()
        this.fileInput.nativeElement.value = ''; // Clear the file input
        return;
      }
      if (file.size > 24 * 1024 * 1024) {
        this.alertWithWarningFileSize()
        this.fileInput.nativeElement.value = ''; // Clear the file input
        return;
      }
      this.selectedFile = file;
    }
  }


  uploadFile() {
    if (!this.selectedFile || !this.thesesId || this.fileInput.invalid) {
      this.alertWithWarningSelect()
      console.error('Invalid form or no file selected.');
      return;
    }

    const uploadData = new FormData();
    uploadData.append('file', this.selectedFile, this.selectedFile.name);

    this.addFilesService.addFiles(uploadData, {
      reportProgress: true,
      observe: 'events'
    })
      .subscribe(event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.uploadProgress = Math.round((event.loaded / (event.total ?? 1)) * 100);
        } else if (event.type === HttpEventType.Response) {
          console.log("fileId in the DB: ")
          console.log(event.body) // WORKS!
          if (event.body > 0) {
            console.log('File (1) is completely uploaded!');
            this.addFilesService.assembleFileIdWithStudentId(Number(event.body), Number(this.thesesId)).subscribe(
              (resp) => {
                this.alertWithSuccess()
                // this.message = 'Sikeresen adatbázisba írva.';
              },
              (err) => {
                this.alertWithError()
                console.log(err.value)
              }
            )
          }
        }
      }, err => {
        //this.message = err.message || 'Could not upload the file!';
        //console.error(err);
      });
  }

  // generateDocx() {
  //   this.documentService.generateDocx().subscribe(
  //     (resp) => {
  //       console.log(resp)
  //     },
  //     (err) => {
  //       this.message = 'error van: ' + err.value;
  //       console.error(err);
  //     }
  //   );
  // }

  getTheses(userId: number) {
    this.addFilesService.findThesesByLoggedInStudent(userId).subscribe(
      (resp) => {
        this.theses = resp
        console.log(resp)
      },
      (err) => {
        this.alertWithErrorMessage(err.value)
      }
    )
  }

}
