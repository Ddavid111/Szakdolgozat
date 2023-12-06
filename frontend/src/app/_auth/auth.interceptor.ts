import {Injectable} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Router} from "@angular/router";
import {AuthService} from "../_services/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(req.headers.get("No-Auth") === "True") {
      return next.handle(req.clone());
    }

    const token = this.authService.getToken()
    if(token) {
      req = this.addToken(req, token);
    }


    return next.handle(req).pipe(
      catchError(
        (err: HttpErrorResponse) => {
            alert("Error " + err.status + "\n" + err.name)
       //   console.log(err.status)
          if(err.status === 401) {
            this.router.navigate(['/login']) // if the user is not logged in, navigate to the login page
          } else if(err.status === 403) {
            this.router.navigate(['/forbidden']) // if the user logged in but try to reach forbidden component, navigate to forbidden
          }
          return throwError('Something went wrong!');
        }
      )
    );
  }

  private addToken(req: HttpRequest<any>, token: string) {
    return req.clone(
      {
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      }
    );
  }
}
