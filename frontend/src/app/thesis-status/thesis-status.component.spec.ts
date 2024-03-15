import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThesisStatusComponent } from './thesis-status.component';

describe('ThesisStatusComponent', () => {
  let component: ThesisStatusComponent;
  let fixture: ComponentFixture<ThesisStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ThesisStatusComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ThesisStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
