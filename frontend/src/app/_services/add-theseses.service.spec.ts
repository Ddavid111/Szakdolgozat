import { TestBed } from '@angular/core/testing';

import { AddThesesesService } from './add-theseses.service';

describe('AddThesesesService', () => {
  let service: AddThesesesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddThesesesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
