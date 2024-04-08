import { Component, OnInit } from '@angular/core';
import { AddFilesService } from "../_services/add-files.service";
import { HttpEventType } from "@angular/common/http";
import { ListThesesesService } from "../_services/list-theseses.service";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-upload-pdf-zipfiles',
  templateUrl: './upload-pdf-zipfiles.component.html',
  styleUrls: ['./upload-pdf-zipfiles.component.css']
})
export class UploadPDFZIPFilesComponent implements OnInit {
  selectedFile1: File | null = null;
  selectedFile2: File | null = null;
  uploadProgress: number = 0;
  uploadProgress2: number = 0;
  // message: string | undefined;

  theses: any;
  thesesId: number | undefined;

  constructor(private addFilesService: AddFilesService, private findThesesService: ListThesesesService) {}

  ngOnInit(): void {
    this.getTheses(localStorage['userId']);
  }

  alertWithWarningFileType()
  {
  Swal.fire("Warning",'Invalid file type. Only PDF and ZIP files are allowed.','warning')
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

  getTheses(userId: number) {
    this.addFilesService.findThesesByLoggedInStudent(userId).subscribe(
      (resp) => {
        this.theses = resp;
        console.log(resp);
      },
      (err) => {
        this.alertWithErrorMessage(err.value)
      }
    );
  }

  onFileSelected(event: any, buttonNumber: number) {
    const file = event.target.files[0];

    if (!this.isValidFile(file, buttonNumber)) {
      this.alertWithWarningFileType()
      event.target.value = '';
      return;
    }

    if (!this.isValidFileSize(file)) {
      this.alertWithWarningFileSize
      return;
    }

    switch (buttonNumber) {
      case 1:
        this.selectedFile1 = file;
        break;
      case 2:
        this.selectedFile2 = file;
        break;
      default:
        this.alertWithError()
        break;
    }
  }

  uploadFile(buttonNumber: number) {
    const uploadData = new FormData();

    switch (buttonNumber) {
      case 1: {
        if (!this.selectedFile1 || !this.thesesId) {
          this.alertWithWarningSelect()
          return;
        }
        uploadData.append('file', this.selectedFile1, this.selectedFile1.name);

        this.addFilesService.addFiles(uploadData, {
          reportProgress: true,
          observe: 'events'
        }).subscribe(res => {
          if (res.type === HttpEventType.UploadProgress) {
            this.uploadProgress = Math.round((res.loaded / (res.total ?? 1)) * 100);
          } else if (res.type === HttpEventType.Response) {
            if (res.body > 0) {
              console.log('File (1) is completely uploaded!');
              this.addFilesService.assembleFileIdWithStudentId(Number(res.body), Number(this.thesesId)).subscribe(
                (resp) => {
                  this.alertWithSuccess()
                  // this.message = 'Sikeresen adatbázisba írva.';
                },
                (err) => {
                  this.alertWithErrorMessage(err.value)
                }
              );
            }
          }
        }, err => {
          this.alertWithError()
          console.error(err);
        });
        break;
      }
      case 2: {
        if (!this.selectedFile2 || !this.thesesId) {
          this.alertWithWarningSelect()
          return;
        }
        uploadData.append('file', this.selectedFile2, this.selectedFile2.name);

        this.addFilesService.addFiles(uploadData, {
          reportProgress: true,
          observe: 'events'
        }).subscribe(res => {
          if (res.type === HttpEventType.UploadProgress) {
            this.uploadProgress2 = Math.round((res.loaded / (res.total ?? 1)) * 100);
          } else if (res.type === HttpEventType.Response) {
            if (res.body > 0) {
              console.log('File (2) is completely uploaded!');
              this.addFilesService.assembleFileIdWithStudentId(Number(res.body), Number(this.thesesId)).subscribe(
                (resp) => {
                  this.alertWithSuccess()
                  // this.message = 'Sikeresen adatbázisba írva.';
                },
                (err) => {
                  alert(err.value);
                }
              );
            }
          }
        }, err => {
          this.alertWithError()
          console.error(err);
        });
        break;
      }
      default:
        alert('Error van.');
        break;
    }
  }

  private isValidFile(file: File | undefined, buttonNumber: number): boolean {
    if (!file) return false;
    const fileName = file.name.toLowerCase();
    if (buttonNumber === 1) {
      return fileName.endsWith('.pdf');
    } else if (buttonNumber === 2) {
      return fileName.endsWith('.zip');
    }
    return false;
  }

  private isValidFileSize(file: File): boolean {
    const fileSizeInMB = file.size / (1024 * 1024);
    return fileSizeInMB <= 24;
  }

}
