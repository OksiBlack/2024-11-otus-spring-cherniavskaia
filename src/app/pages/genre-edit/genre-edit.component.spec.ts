import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenreEditComponent } from './genre-edit.component';

describe('GenreEditComponent', () => {
  let component: GenreEditComponent;
  let fixture: ComponentFixture<GenreEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenreEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GenreEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
