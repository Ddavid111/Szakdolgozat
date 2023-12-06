import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadPDFZIPFilesComponent } from './upload-pdf-zipfiles.component';

describe('UploadPDFZIPFilesComponent', () => {
  let component: UploadPDFZIPFilesComponent;
  let fixture: ComponentFixture<UploadPDFZIPFilesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadPDFZIPFilesComponent]
    });
    fixture = TestBed.createComponent(UploadPDFZIPFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
