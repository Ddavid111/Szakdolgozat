import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FindAllUsersComponent } from "./find-allUsers.component";

describe('FindAllUsersComponent', () => {
  let component: FindAllUsersComponent;
  let fixture: ComponentFixture<FindAllUsersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FindAllUsersComponent]
    });
    fixture = TestBed.createComponent(FindAllUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
