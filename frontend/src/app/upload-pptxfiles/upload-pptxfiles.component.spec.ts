import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadPPTXFilesComponent } from './upload-pptxfiles.component';

describe('UploadPPTXFilesComponent', () => {
  let component: UploadPPTXFilesComponent;
  let fixture: ComponentFixture<UploadPPTXFilesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UploadPPTXFilesComponent]
    });
    fixture = TestBed.createComponent(UploadPPTXFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
