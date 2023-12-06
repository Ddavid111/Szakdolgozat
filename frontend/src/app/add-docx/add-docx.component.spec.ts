import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddDocxComponent } from './add-docx.component';

describe('AddDocxComponent', () => {
  let component: AddDocxComponent;
  let fixture: ComponentFixture<AddDocxComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddDocxComponent]
    });
    fixture = TestBed.createComponent(AddDocxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
