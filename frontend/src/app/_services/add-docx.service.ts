import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddDocxService {

  constructor(private http: HttpClient) {
  }

  API = 'http://localhost:8080'

  public generateReview(docxData: any, userId: any) {

    docxData.userId = userId
    console.log(docxData)
    return this.http.post(this.API + '/generateReview' , docxData, {
      responseType: 'arraybuffer',
      observe: 'response'
    })
  }

  public addDocx(docxData: any, userId: any) {
    docxData.userId = userId
    console.log(docxData)
    return this.http.post(this.API + '/generateDocx', docxData, {
      responseType: 'arraybuffer',
      observe: 'response'
    })
  }
}
