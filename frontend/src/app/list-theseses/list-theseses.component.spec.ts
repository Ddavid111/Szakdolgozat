import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListThesesesComponent } from './list-theseses.component';

describe('ListThesesesComponent', () => {
  let component: ListThesesesComponent;
  let fixture: ComponentFixture<ListThesesesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListThesesesComponent]
    });
    fixture = TestBed.createComponent(ListThesesesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
