import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddThesesesComponent } from './add-theseses.component';

describe('AddThesesesComponent', () => {
  let component: AddThesesesComponent;
  let fixture: ComponentFixture<AddThesesesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddThesesesComponent]
    });
    fixture = TestBed.createComponent(AddThesesesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
