import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowBalComponent } from './show-bal.component';

describe('ShowBalComponent', () => {
  let component: ShowBalComponent;
  let fixture: ComponentFixture<ShowBalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowBalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowBalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
