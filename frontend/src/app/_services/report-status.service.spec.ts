import { TestBed } from '@angular/core/testing';

import { ReportStatusService } from './report-status.service';

describe('ReportStatusService', () => {
  let service: ReportStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportStatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
