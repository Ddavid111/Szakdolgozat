import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddTopicsService {

  constructor(private http: HttpClient) { }

  API = 'http://localhost:8080'

  public addTopics(topicsData: any){
    return this.http.post(this.API + '/addTopics', topicsData)
  }
}
