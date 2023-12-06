import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddFilesService {

  constructor(private http: HttpClient) { }

  API = 'http://localhost:8080'

  public addFiles(FilesData: any) {
    return this.http.post(this.API + '/addFiles', FilesData)
  }
}
