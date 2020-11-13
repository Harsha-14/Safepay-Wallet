import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AccDetailsComponent } from './acc-details.component';

describe('AccDetailsComponent', () => {
  let component: AccDetailsComponent;
  let fixture: ComponentFixture<AccDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AccDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
