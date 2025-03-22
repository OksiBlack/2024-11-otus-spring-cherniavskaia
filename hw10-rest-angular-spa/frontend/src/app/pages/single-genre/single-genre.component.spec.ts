import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleGenreComponent } from './single-genre.component';

describe('SingleGenreComponent', () => {
  let component: SingleGenreComponent;
  let fixture: ComponentFixture<SingleGenreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SingleGenreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SingleGenreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
