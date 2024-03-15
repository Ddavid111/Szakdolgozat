import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ListThesesesComponent} from './list-theseses/list-theseses.component';
import {FindAllUsersComponent} from "./find-allUsers/find-allUsers.component";
import {AddUsersComponent} from './add-users/add-users.component';
import {AddThesesesComponent} from './add-theseses/add-theseses.component';
import {UploadPDFZIPFilesComponent} from './upload-pdf-zipfiles/upload-pdf-zipfiles.component';
import {UploadPPTXFilesComponent} from './upload-pptxfiles/upload-pptxfiles.component';
import {AddTopicsComponent} from './add-topics/add-topics.component';
import {HeaderComponent} from './header/header.component';
import {AddSessionComponent} from './add-session/add-session.component';
import {AddDocxComponent} from './add-docx/add-docx.component';
import {AuthGuard} from "./_auth/auth.guard";
import {AuthInterceptor} from "./_auth/auth.interceptor";
import {UserService} from "./_services/user.service";
import {LoginComponent} from './login/login.component';
import {ForbiddenComponent} from './forbidden/forbidden.component';
import {MatSelectModule} from "@angular/material/select";
import {NgxPaginationModule} from 'ngx-pagination';
import {Ng2OrderModule} from "ng2-order-pipe";
import {Ng2SearchPipeModule} from 'ng2-search-filter';
import {MyprofileComponent} from './myprofile/myprofile.component';
import {ZvReportComponent} from './zv-report/zv-report.component';
import {ReportStatusComponent} from './report-status/report-status.component';
import {MatExpansionModule} from "@angular/material/expansion";
import { ThesisStatusComponent } from './thesis-status/thesis-status.component';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { ListSessionsComponent } from './list-sessions/list-sessions.component';



@NgModule({
  declarations: [
    AppComponent,
    ListThesesesComponent,
    FindAllUsersComponent,
    AddUsersComponent,
    AddThesesesComponent,
    UploadPDFZIPFilesComponent,
    UploadPPTXFilesComponent,
    AddTopicsComponent,
    HeaderComponent,
    AddSessionComponent,
    AddDocxComponent,
    LoginComponent,
    ForbiddenComponent,
    MyprofileComponent,
    ZvReportComponent,
    ReportStatusComponent,
    ThesisStatusComponent,
    ListSessionsComponent,
    ListSessionsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NgxPaginationModule,
    Ng2OrderModule,
    Ng2SearchPipeModule,
    MatSelectModule,
    MatExpansionModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule
  ],
  providers: [
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    { provide: MAT_DATE_LOCALE, useValue: 'hu-HU' },
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
