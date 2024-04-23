import { TestBed } from '@angular/core/testing';

import { AddDocxService } from './add-docx.service';

describe('AddDocxService', () => {
  let service: AddDocxService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddDocxService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
