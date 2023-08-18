import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PetDetailComponent } from './pet-detail.component';

describe('Pet Management Detail Component', () => {
  let comp: PetDetailComponent;
  let fixture: ComponentFixture<PetDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PetDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pet: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PetDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PetDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pet on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
