import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ReportStatusService {

  constructor(private http: HttpClient) {
  }

  API = "http://localhost:8080"

  public getDownloadableThesisFiles() {
    return this.http.get(this.API + '/getFileList')
  }

  public downloadFile(uuid: string) {
    return this.http.get(this.API + '/downloadFile?uuid=' + uuid, {
      responseType: 'arraybuffer',
      observe: 'response'
    })
  }

  public requestForReview(userId: number, thesisId: number) {
    let httpParams = new HttpParams()
      .append('userId', userId)
      .append('thesisId', thesisId)

    console.log(httpParams)

    return this.http.post(this.API + '/requestForReview', httpParams)
  }

  public findThesesUnderReview(isUnderReview: boolean){
    return this.http.get(this.API+ '/findThesesUnderReview?isUnderReview=' + isUnderReview)
  }

  public findReviewedTheses() {
    return this.http.get(this.API+ '/findReviewedTheses')
  }

  public getReviewData() {
    return this.http.get(this.API + '/getReviewData')
  }

  public deleteFile(uuid: string) {
    return this.http.delete(this.API + '/deleteFile?uuid=' + uuid)
  }


  public findFilesByThesesId(thesisId: number){
    return this.http.get(this.API + '/findFilesByThesesId?thesisId=' + thesisId)
  }

}
