import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ListThesesesComponent} from "./list-theseses/list-theseses.component";
import {FindAllUsersComponent} from "./find-allUsers/find-allUsers.component";
import {AddUsersComponent} from "./add-users/add-users.component";
import {AddThesesesComponent} from "./add-theseses/add-theseses.component";
import {UploadPPTXFilesComponent} from "./upload-pptxfiles/upload-pptxfiles.component";
import {UploadPDFZIPFilesComponent} from "./upload-pdf-zipfiles/upload-pdf-zipfiles.component";
import {AddTopicsComponent} from "./add-topics/add-topics.component";
import {AddReviewsComponent} from "./add-reviews/add-reviews.component";
import {AddSessionComponent} from "./add-session/add-session.component";
import {AddDocxComponent} from "./add-docx/add-docx.component";
import {LoginComponent} from "./login/login.component";
import {ForbiddenComponent} from "./forbidden/forbidden.component";


const routes: Routes = [
  {
  path: 'listTheseses', component: ListThesesesComponent
  },
  {
    path: 'findAllUsers', component: FindAllUsersComponent
  },
  {
    path: 'addUser', component: AddUsersComponent
  },
  {
    path: 'addTheseses', component: AddThesesesComponent
  },
  {
    path: 'uploadPDFZIP', component: UploadPDFZIPFilesComponent
  },
  {
    path: 'uploadPPTX', component: UploadPPTXFilesComponent
  },
  {
    path: 'addTopics', component: AddTopicsComponent
  },
  {
    path: 'addReviews', component: AddReviewsComponent
  },
  {
    path: 'addSession', component: AddSessionComponent
  },
  {
    path: 'addDocx', component: AddDocxComponent
  },
  {
    path: 'login', component: LoginComponent
  },
  {
    path: 'forbidden', component: ForbiddenComponent
  }
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
