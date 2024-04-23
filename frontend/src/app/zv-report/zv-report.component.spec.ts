import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZvReportComponent } from './zv-report.component';

describe('ZvReportComponent', () => {
  let component: ZvReportComponent;
  let fixture: ComponentFixture<ZvReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ZvReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ZvReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
