import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ListThesesesService {

  constructor(private http: HttpClient) {
  }

  API = "http://localhost:8080"

  public getThesesList() {
    return this.http.get(this.API + '/getThesesList')
  }

  public getThesesListToDisplay() {
    return this.http.get(this.API + '/getThesesListToDisplay')
  }

  public updateTheses(theses: any) {
    return this.http.put(this.API + '/updateTheses', theses)
  }

  public deleteTheses(id: any) {
    return this.http.delete(this.API + '/deleteTheses?id=' + id)
  }

  public setTopicForStudent(topicId: any, thesisId: any, topic_score: any) {
    let httpParams = new HttpParams()
      .append("topicId", topicId)
      .append("thesisId", thesisId)
      .append("topic_score", topic_score)

    return this.http.post(this.API + '/chooseTopic', httpParams)
  }

  public getTopicList() {
    return this.http.get(this.API + '/getTopics')
  }

  public findThesesByUserId(userId: number)
  {
    return this.http.get(this.API + '/findThesesByUserId?userId=' + userId)
  }

  public findThesesByUserIdAndReviewerId(userId: number, reviewerId: number)
  {
    return this.http.get(this.API + '/findThesesByUserIdAndReviewerId?userId=' + userId + '&reviewerId=' + reviewerId)
  }

}
