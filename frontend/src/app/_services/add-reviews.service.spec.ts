import { TestBed } from '@angular/core/testing';

import { AddReviewsService } from './add-reviews.service';

describe('AddReviewsService', () => {
  let service: AddReviewsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddReviewsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
