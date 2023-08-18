import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LikeeDetailComponent } from './likee-detail.component';

describe('Likee Management Detail Component', () => {
  let comp: LikeeDetailComponent;
  let fixture: ComponentFixture<LikeeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LikeeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ likee: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LikeeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LikeeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load likee on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.likee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
