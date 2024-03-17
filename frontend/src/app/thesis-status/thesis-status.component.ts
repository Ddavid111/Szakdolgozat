import {Component, OnInit} from '@angular/core';
import {ReportStatusService} from "../_services/report-status.service";
import {ListThesesesService} from "../_services/list-theseses.service";
import {findAllUsersService} from "../_services/find-allUsers.service";
import Swal from "sweetalert2";

@Component({
  selector: 'app-thesis-status',
  templateUrl: './thesis-status.component.html',
  styleUrls: ['./thesis-status.component.css']
})
export class ThesisStatusComponent implements OnInit{

  theses: any
  title: any
  neptunCode: any
  fullname: any
  supervisorname: any
  reviewername: any
  temp = null as any
  reviews: any
  users: any
  reviewers = [] as any
  thesisToDisplay = {
    title: null,
    thesisId: null
  }

  thesesFullData = [] as any // a fájlok (pdf, pptx, zip) letöltéséhez szükséges adatok vannak benne

  files = [] as any

  selectedReviewer: any

  constructor(
    private reportStatusService: ReportStatusService,
    private listThesesService: ListThesesesService,
    private findallUsersService: findAllUsersService
  )
  { }

  ngOnInit(): void {
     this.getReviewList(localStorage['userId'])


  }

  alertWithFileType(){
    Swal.fire("Hiba", 'probléma a fájlok típusának azonosítása során',  'error')
  }

  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error')
  }

  alertFileDeleted(filename: any){
    Swal.fire("Siker", 'Fájl' + filename + 'sikeresen törlésre került',  'success')
  }

  showErrorMessage() {
  Swal.fire('Hiba', 'Nincs PDF fájl a törléshez!', 'error')
  }


  showConfirmDialog(title: string, text: string): Promise<boolean> {
    return Swal.fire({
      title: title,
      text: text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Igen, töröld!',
      cancelButtonText: 'Mégse'
    }).then((result) => {
      return result.isConfirmed;
    });
  }


  correctTable() {
    console.log(this.theses)
    this.theses.forEach((x: any) => { // végigmegyünk a thesiseken
      let thesisId = x.id
      let thesisTitle = x.title
      let submissionDate_ = x.submissionDate
      let studentId = x.user.userId
      // let supervisorId = ''
      // if(x.supervisor !== undefined && x.supervisor !== null) {
      //   supervisorId = x.supervisor.userId
      // }

      let supervisorId = x.supervisor.userId

      let dataForOneThesis = { // lokálisan az aktuális thesishez tartozó cuccokat ebbe szedjük össze
        neptunCode: '',
        fullname: '',
        supervisorname: '',
        reviewername: '',
        submissionDate: '',
        title: '',
        pdfUUID: null,
        pptUUID: null,
        attachmentUUID: null
      }
      dataForOneThesis.submissionDate = submissionDate_
      dataForOneThesis.title = thesisTitle

      let tempReviewerId: any = null

      this.reviews.forEach((r: any) => { // bíráló megkeresése
        if(thesisId === r.theseses.id)
        {
          tempReviewerId = r.user.userId
          console.log(tempReviewerId)
        }
      })


      this.users.forEach((a: any)=> { // hallgató nevének, neptun kódjának megkeresése

        console.log(a)
        if(studentId === a.userId) {
          dataForOneThesis.fullname = a.fullname
          dataForOneThesis.neptunCode = a.neptunCode
        }

        if(supervisorId == a.userId) {         // thesishez tartozó témavezető megkeresése
          dataForOneThesis.supervisorname = a.fullname
        }

        if(tempReviewerId != null && tempReviewerId ==  a.userId) {
          dataForOneThesis.reviewername = a.fullname // bíráló neve
        }
      })

      // console.log("current thesis ID and title: " + thesisId + " " + thesisTitle + " reviewer id: " + tempReviewerId)
      console.log(dataForOneThesis)
      this.files.forEach((y: any) => {
        console.log(y)
        if(y.first === thesisId) {
          // ekkor megvan az aktuális thesis a file táblában
          let fileType = y.second.substring(0, 2) // pd: pdf, at: zip, pp: ppt
          switch (fileType) {
            case 'pd': { //pdf
              dataForOneThesis.pdfUUID = y.third
              break
            }
            case 'at': { // attachment
              dataForOneThesis.attachmentUUID = y.third
              break
            }
            case 'pp': { // ppt
              dataForOneThesis.pptUUID = y.third
              break
            }
            default: {
              this.alertWithFileType()
              break
            }
          }
        }
      })
      let roleId = localStorage['roles']
      let userId = localStorage['userId']
      console.log(userId)
      console.log(tempReviewerId)
      console.log(dataForOneThesis)

      if(roleId == 3)
      {
        if(tempReviewerId == userId) {
          console.log(tempReviewerId)
          this.thesesFullData.push(dataForOneThesis)
          this.temp = this.thesesFullData
        }
      }
      else {
        this.thesesFullData.push(dataForOneThesis)
        this.temp = this.thesesFullData
      }
    })

  }


  downloadFile(uuid: string) {
    this.reportStatusService.downloadFile(uuid)
      .subscribe(response => {
        const contentDispositionHeader = response.headers.get('Content-Disposition');
        const matches = /filename=(.+)$/.exec(contentDispositionHeader as string);
        const filename = matches ? matches[1] : 'noname';
        const blob = new Blob([response.body as BlobPart], {type: 'application/octet-stream'});
        const downloadLink = document.createElement('a');
        const url = window.URL.createObjectURL(blob);

        downloadLink.href = url;
        downloadLink.download = filename;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
        window.URL.revokeObjectURL(url);
      }, error => {
        console.error('Error downloading file', error);
      });
  }

  getDownloadableThesisFiles(thesesIds?: any[], roleId?: any) {
    this.reportStatusService.getDownloadableThesisFiles().subscribe(
      (resp : any) => {
        if((roleId == 0 || roleId == 4) && thesesIds !== undefined){
        // let tempResp: any = resp
        resp.forEach((x:any)=>{
          if(thesesIds?.includes(x.first)){
            this.files.push(x)
          }
        })
          this.correctTable()
        }
        else{
        console.log(resp)
        this.files = resp

        this.correctTable()
        }
      },
      (err) => {
        console.error(err)
      }
    )
  }

  getThesesList(userId?: number, roleId?: number) {
    this.listThesesService.getThesesList().subscribe(
      (resp : any) => {
        console.log(resp)
        if(roleId == 0){
        // let tempResp: any = resp
        this.theses = resp.filter((x: any) =>
            x.userId == userId
        )
          console.log(userId)
          console.log(this.theses)
          // @ts-ignore
          let thesesIds = this.theses.map(({id}) => id)
          console.log(thesesIds)
        this.getDownloadableThesisFiles(thesesIds,roleId)
        }
        else if(roleId == 4){
          // let tempResp: any = resp
          this.theses = resp.filter((x: any) =>
            x.supervisor.userId == userId
          )
          console.log(userId)
          console.log(this.theses)
          // @ts-ignore
          let thesesIds = this.theses.map(({id}) => id)
          console.log(thesesIds)
          this.getDownloadableThesisFiles(thesesIds,roleId)
        }
        else{
          this.theses = resp
          this.getDownloadableThesisFiles(undefined, roleId)
        }
      },
      (err) =>
      {
        this.alertWithError(err.value)
      }
    )
  }

  getUsersList(userId?: number, roleId?: number){
    this.findallUsersService.findAllUsers().subscribe(
      (resp : any) => {
        if(roleId == 0){
          // let tempResp : any = resp
          console.log(resp)
          this.users = resp.filter((x: any) =>
              (x.userId == userId && x.roleId == 0) ||  x.roleId == 3 || x.roleId == 4
          )
          this.getThesesList(userId, roleId)
        }
        else if(roleId == 4){
          // let tempResp : any = resp
          console.log(resp)
          this.users = resp.filter((x: any) =>
            (x.userId == userId && x.roleId == 4) ||  x.roleId == 3 || x.roleId == 0
          )
          this.getThesesList(userId, roleId)
        }
        else if(roleId == 3){
          // let tempResp : any = resp
          console.log(resp)
          this.users = resp.filter((x: any) =>
            x.roleId == 0 || (x.userId == userId && x.roleId == 3) ||  x.roleId == 4
          )
          this.getThesesList(userId, roleId)
        }
        else{
          this.users = resp
          this.getThesesList()
        }

      },
      (err) => {
        alert(err.value)
      }
    )
  }

  getReviewList(userId: number){
    console.log(userId)
    let roleId = localStorage['roles']

    this.reportStatusService.getReviewData().subscribe(
      (resp: any) => {
        if(roleId == 0)
        {
          // let tempResp : any = resp
        this.reviews = resp.filter((element: any) =>
            element.theseses.userId == userId
        )
        console.log("reviews:")
        console.log(resp)

        this.getUsersList(userId, roleId)
          }
        else if(roleId == 4)
        {
          // let tempResp : any = resp
          this.reviews = resp.filter((element: any) =>
            element.theseses.supervisor.userId == userId
          )
          console.log("reviews:")
          console.log(resp)

          this.getUsersList(userId, roleId)
        }
        else if(roleId == 3)
        {
          // let tempResp : any = resp
          this.reviews = resp.filter((element: any) =>
            element.user.userId == userId
          )
          console.log("reviews:")
          console.log(this.reviews)

          this.getUsersList(userId, roleId)
        }
        else{
          this.reviews = resp
          console.log("reviews:")
          console.log(resp)

          this.getUsersList()
        }
      },
      (err) => {
        alert(err.value)
      }
    )
  }

  deletePdfFiles() {
    let pdfFileFound = false;
    for (let file of this.files) {
      if (file.second.endsWith('.pdf')) {
        pdfFileFound = true;
        this.showConfirmDialog('Biztosan törli ezt a fájlt?', 'Ez a fájl véglegesen törlésre kerül!').then((confirmed) => {
          if (confirmed) {
            this.reportStatusService.deleteFile(file.third).subscribe(
              (resp) => {
                  window.location.reload()
              },
              (err) => {
                console.error('Hiba történt a PDF fájl törlése közben:', err);
              }
            );
          }
        });
      }
    }
    if (!pdfFileFound) {
      // // @ts-ignore
      // this.showErrorMessage()
      (<HTMLInputElement> document.getElementById("deletePdfButton")).disabled = true;
    }
    else {
      (<HTMLInputElement> document.getElementById("deletePdfButton")).disabled = false;
    }
  }


  deleteZipFiles() {
    let zipFileFound = false;
    for (let file of this.files) {
      if (file.second.endsWith('.zip')) {
        zipFileFound = true;
        this.showConfirmDialog('Biztosan törli ezt a fájlt?', 'Ez a fájl véglegesen törlésre kerül!').then((confirmed) => {
          if (confirmed) {
            this.reportStatusService.deleteFile(file.third).subscribe(
              (resp) => {
                this.alertFileDeleted(file.second);
                window.location.reload();
              },
              (err) => {
                console.error('Hiba történt a ZIP fájl törlése közben:', err);
              }
            );
          }
        });
      }
    }
    if (!zipFileFound) {
      this.showErrorMessage()
    }
  }

  deletePptxFiles() {
    let pptxFileFound = false;
    for (let file of this.files) {
      if (file.second.endsWith('.pptx')) {
        pptxFileFound = true;
        this.showConfirmDialog('Biztosan törli ezt a fájlt?', 'Ez a fájl véglegesen törlésre kerül!').then((confirmed) => {
          if (confirmed) {
            this.reportStatusService.deleteFile(file.third).subscribe(
              (resp) => {
                window.location.reload();
              },
              (err) => {
                this.alertWithError(err)
              }
            );
          }
        });
      }
    }
    if (!pptxFileFound) {
      this.showErrorMessage()
    }
  }




  search(criteria: string, field: string) {
    if (criteria == "") {
      this.thesesFullData = this.temp;
    } else {
      this.thesesFullData = this.temp.filter((res: any) => {

      return res[field].toLocaleLowerCase().match(criteria.toLocaleLowerCase());
      });
    }
  }

  searchForTitle() {
    this.search(this.title, 'title');
  }

  searchForNeptunCode() {
    this.search(this.neptunCode, 'neptunCode');
  }

  searchForStudent(){
    this.search(this.fullname, 'fullname')
  }

  searchForSupervisor(){
    this.search(this.supervisorname,'supervisorname')
  }

  searchForReviewer(){
    this.search(this.reviewername,'reviewername')
  }


}