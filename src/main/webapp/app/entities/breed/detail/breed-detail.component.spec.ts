import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BreedDetailComponent } from './breed-detail.component';

describe('Breed Management Detail Component', () => {
  let comp: BreedDetailComponent;
  let fixture: ComponentFixture<BreedDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BreedDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ breed: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BreedDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BreedDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load breed on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.breed).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
