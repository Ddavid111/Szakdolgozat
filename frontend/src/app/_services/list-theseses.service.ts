import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ListThesesesService {

  constructor(private http: HttpClient) { }

  API = "http://localhost:8080"

  public getThesesList() {
    return this.http.get(this.API + '/getThesesList')
  }

  public updateTheses(theses : any){
    return this.http.put(this.API + '/updateTheses',theses)
  }

  public deleteTheses(id : any){
  //  var idToBackend: number = +id;
    return this.http.delete(this.API + '/deleteTheses?id='+id)
  }

}
