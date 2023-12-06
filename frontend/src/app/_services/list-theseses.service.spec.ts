import { TestBed } from '@angular/core/testing';

import { ListThesesesService } from './list-theseses.service';

describe('ListThesesesService', () => {
  let service: ListThesesesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListThesesesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
