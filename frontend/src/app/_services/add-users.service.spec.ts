import { TestBed } from '@angular/core/testing';

import { AddUsersService } from './add-users.service';

describe('AddUsersService', () => {
  let service: AddUsersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddUsersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
