import { TestBed } from '@angular/core/testing';

import { AddTopicsService } from './add-topics.service';

describe('AddTopicsService', () => {
  let service: AddTopicsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddTopicsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
