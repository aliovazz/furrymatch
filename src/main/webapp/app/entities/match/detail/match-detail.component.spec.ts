import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MatchDetailComponent } from './match-detail.component';

describe('Match Management Detail Component', () => {
  let comp: MatchDetailComponent;
  let fixture: ComponentFixture<MatchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MatchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ match: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MatchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MatchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load match on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.match).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
