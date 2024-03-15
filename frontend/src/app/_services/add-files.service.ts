import {Injectable} from '@angular/core';
import {HttpClient, HttpRequest, HttpEvent, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddFilesService {

  constructor(private http: HttpClient) {
  }

  API = 'http://localhost:8080';

  public addFiles(file: any, options?: any): Observable<HttpEvent<any>> {
    const req = new HttpRequest('POST', this.API + '/addFiles', file, options);
    return this.http.request(req);
  }

  public assembleFileIdWithStudentId(fileId: number, thesisId: number) {
    let httpParams = new HttpParams()
      .append("fileId", fileId)
      .append("thesisId", thesisId)
    return this.http.post(this.API + '/assembleFileWithStudent', httpParams)
  }
}
