import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedListComponent } from './need-list.component';

describe('NeedListComponent', () => {
  let component: NeedListComponent;
  let fixture: ComponentFixture<NeedListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NeedListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NeedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
