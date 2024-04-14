import {Component, OnInit} from '@angular/core';
import {ReportStatusService} from "../_services/report-status.service";
import {ListThesesesService} from "../_services/list-theseses.service";
import {findAllUsersService} from "../_services/find-allUsers.service";
import Swal from "sweetalert2";
import {UserService} from "../_services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-report-status',
  templateUrl: './report-status.component.html',
  styleUrls: ['./report-status.component.css']
})
export class ReportStatusComponent implements OnInit {

  theses: any
  reviews: any
  users: any
  thesesId: any

  reviewers: any
  thesisToDisplay = {
    title: null,
    thesisId: null
  }
  thesesUnderReview = [] as any

  reviewedTheses = [] as any

  thesesFullData = [] as any // a fájlok (pdf, pptx, zip) letöltéséhez szükséges adatok vannak benne

  files = [] as any
  selectedReviewer: any
  thesisId: any

  constructor(
    private reportStatusService: ReportStatusService,
    private listThesesService: ListThesesesService,
    private findallUsersService: findAllUsersService,
    private router: Router,
    private userService: UserService,
  ) {
  }

  alertWithReview() {
    Swal.fire("Hiba", 'Hiba történt a bírálatra felkéréskor.',  'error');
  }
  alertWithError(err: any) {
    Swal.fire("Hiba", ' Error:' + err,  'error');
  }
  alertWithSucces()
  {
    Swal.fire({
      title: "Sikeres",
      icon: 'success',
      allowOutsideClick: false
    }).then((result) => {
      if(!result.isDenied){
        window.location.reload()
      }
      // return result.isConfirmed;
    });
  }

  alertWithFileType(){
    Swal.fire("Hiba", 'probléma a fájlok típusának azonosítása során',  'error');
  }

  alertWithWarning()
  {
    Swal.fire("Figyelem!","Nincs jogosultsága a következő odalhoz!", 'warning')
  }

  ngOnInit(): void {
    if (!this.userService.roleMatch([
      "Elnök",
      "Jegyző",
      "ADMIN",
    ])) { // if the roles are not correct, navigate to login page before the component would have loaded
      this.alertWithWarning()
      this.router.navigate(['/login'])
    }

    // review list --> users list --> thesis list --> downloadable
    this.getReviewList()

    this.getReviewerToDropdown()

  }


  findReviewedTheses() {
    this.reportStatusService.findReviewedTheses().subscribe(
      (resp) => {
        this.reviewedTheses = resp
        console.log(resp)
        console.log(this.reviews)
        let reviewedTheses = [] as any
        this.reviewedTheses.forEach((thesisUnderReview: any) => {
          let tempRecord = this.thesesFullData.filter((thesisFullDataRecord: any) => thesisFullDataRecord.thesisId === thesisUnderReview.id)
          if(tempRecord.size != 0)
            reviewedTheses.push(tempRecord[0])
        })
        this.reviewedTheses = reviewedTheses
        // this.reviews.forEach((review: any) => {
        //   if(review.theseses.id == this.reviewedTheses.id)
        //   {
        //     this.reviewedTheses.reviewerfullname = this.reviews.user.fullname
        //   }
        // })
        console.log(this.reviewedTheses)

      }
    )
  }



  findThesesUnderReview(){
    this.reportStatusService.findThesesUnderReview(true).subscribe(
      (resp) =>{
        this.thesesUnderReview = resp

        let thesesUnderReview_new = [] as any
         this.thesesUnderReview.forEach((thesisUnderReview: any) => {
           let tempRecord = this.thesesFullData.filter((thesisFullDataRecord: any) => thesisFullDataRecord.thesisId === thesisUnderReview.id)
           console.log(tempRecord)
           console.log(this.thesesFullData)
           if(tempRecord.size != 0)
           thesesUnderReview_new.push(tempRecord[0])
         })

        this.thesesUnderReview = thesesUnderReview_new

        console.log(this.thesesUnderReview)

      },
      (err) =>
      {
       this.alertWithError(err.value)
      }
    )

  }

  correctTable() {

    this.theses.forEach((thesis: any) => { // végigmegyünk a thesiseken
      console.log(this.theses)
      let thesisId = thesis.id
      let thesisTitle = thesis.title
      let submissionDate_ = thesis.submissionDate
      let studentId = thesis.user.id
      let supervisiorId = thesis.supervisorId

      let dataForOneThesis = { // lokálisan az aktuális thesishez tartozó cuccokat ebbe szedjük össze
        thesisId: null,
        neptunCode: null,
        fullname: null,
        supervisorname: null,
        reviewername: null,
        submissionDate: null,
        title: null,
        pdfUUID: null,
        pptUUID: null,
        attachmentUUID: null,
        reviewUUID: null,
        secondReviewUUID: null,
        zvUUID: null
      }
      let dateObj = new Date(submissionDate_)
      let monthInCorrectForm = (dateObj.getMonth() + 1).toString()
      if (monthInCorrectForm.length == 1) {
        monthInCorrectForm = '0' + monthInCorrectForm
      }

      let dayInCorrectForm = dateObj.getDate().toString()
      if (dayInCorrectForm.toString().length == 1) {
        dayInCorrectForm = "0" + dayInCorrectForm
      }
      submissionDate_ = dateObj.getFullYear() + '.' + monthInCorrectForm + '.' + dayInCorrectForm + '.'
      dataForOneThesis.thesisId = thesisId
      dataForOneThesis.submissionDate = submissionDate_
      dataForOneThesis.title = thesisTitle

      let tempReviewerId: any = null
      console.log(this.reviews)
      this.reviews.forEach((review: any) => { // bíráló megkeresése, bírálatokon megy végig
        if (thesisId == review.theseses.id && review.user.role == 'Bíráló') {
          tempReviewerId = review.user.id
        }
      })

      this.users.forEach((user: any) => {
        if (studentId == user.id) {              // hallgató nevének, neptun kódjának megkeresése
          dataForOneThesis.fullname = user.fullname
          dataForOneThesis.neptunCode = user.neptunCode
        }

        if (supervisiorId == user.id) {         // thesishez tartozó témavezető megkeresése
          dataForOneThesis.supervisorname = user.fullname
        }

        if (tempReviewerId != null && tempReviewerId == user.id) {
          dataForOneThesis.reviewername = user.fullname // bíráló neve
        }
      })
      let reviewCount = 0
      this.files.forEach((fileResultSet: any) => {
        if (fileResultSet.first === thesisId) {
          // ekkor megvan az aktuális thesis a file táblában
          let fileType = fileResultSet.second.substring(0, 2) // pd: pdf, at: zip, pp: ppt
          switch (fileType) {
            case 'pd': { //pdf
              dataForOneThesis.pdfUUID = fileResultSet.third
              break
            }
            case 'at': { // attachment
              dataForOneThesis.attachmentUUID = fileResultSet.third
              break
            }
            case 'pp': { // ppt
              dataForOneThesis.pptUUID = fileResultSet.third
              break
            }
            case 're': {
              if (reviewCount == 0) {
                dataForOneThesis.reviewUUID = fileResultSet.third
              }
              else {
                dataForOneThesis.secondReviewUUID = fileResultSet.third
              }
              reviewCount++
              break
            }
            case 'zv': { // ppt
              dataForOneThesis.zvUUID = fileResultSet.third
              break
            }
            default: {
              this.alertWithFileType()
              break
            }
          }
        }
      })

      this.thesesFullData.push(dataForOneThesis)
    })
    console.log(this.thesesFullData)
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
        console.error('Error downloading file', error)
      });
  }

  getDownloadableThesisFiles() {
    this.reportStatusService.getDownloadableThesisFiles().subscribe(
      (resp) => {
        console.log(resp)
        this.files = resp
        this.correctTable() // making the 1st table from the top

        this.findThesesUnderReview()
        this.findReviewedTheses()

      },
      (err) => {
        console.error(err)
      }
    )
  }

  requestForReview(record: any) {
    console.log(record)
    this.thesisToDisplay.title = record.title
    this.thesisToDisplay.thesisId = record.id
  }


  chooseReviewer() {
    // ha kiválasztottuk a bírálót és okéztuk, ide jövünk
    let thesesId = this.thesisToDisplay.thesisId
    let userId = this.selectedReviewer

    this.reportStatusService.requestForReview(userId, Number(thesesId)).subscribe(
      (resp) => {
        console.log(resp)
        window.location.reload()
      },
      (err) => {
        this.alertWithReview()
        console.error(err)
      }
    )
  }

  findFilesByThesesId(thesisId: number){
    console.log(thesisId)
    this.reportStatusService.findFilesByThesesId(thesisId).subscribe(
      (resp) => {
        console.log(resp)
        this.alertWithSucces()
      },
      (err) => {
        console.error(err)
      }
    )

  }


  getReviewerToDropdown() {
    // Param: role ID of reviewer (currently it is 3)
    this.findallUsersService.findUsersByRole(3).subscribe(
      (resp) => {
        this.reviewers = resp
      }
    )
  }

  getThesesList() {
    this.listThesesService.getThesesList().subscribe(
      (resp) => {
        this.theses = resp
        this.getDownloadableThesisFiles()
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }

  getUsersList() {
    this.findallUsersService.findAllUsers().subscribe(
      (resp) => {
        this.users = resp
        this.getThesesList()
      },
      (err) => {
       this.alertWithError(err.value)
      }
    )
  }

  getReviewList() {
    this.reportStatusService.getReviewData().subscribe(
      (resp) => {
        this.reviews = resp
        this.getUsersList()
      },
      (err) => {
        this.alertWithError(err.value)
      }
    )
  }



}
