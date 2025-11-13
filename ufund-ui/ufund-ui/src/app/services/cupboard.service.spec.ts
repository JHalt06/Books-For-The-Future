import { TestBed } from '@angular/core/testing';

import { CupboardService } from './cupboard.service';

describe('CupboardService', () => {
  let service: CupboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CupboardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
