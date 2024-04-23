import { TestBed } from '@angular/core/testing';

import { findAllUsersService } from './find-allUsers.service';

describe('findAllUsersService', () => {
  let service: findAllUsersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(findAllUsersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
