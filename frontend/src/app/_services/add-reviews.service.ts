import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddReviewsService {

  constructor(private http: HttpClient) { }

  API = 'http://localhost:8080'

  public addReviews(reviewsData: any){
    return this.http.post(this.API + '/addReviews', reviewsData)
  }

}
