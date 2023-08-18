import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OwnerDetailComponent } from './owner-detail.component';

describe('Owner Management Detail Component', () => {
  let comp: OwnerDetailComponent;
  let fixture: ComponentFixture<OwnerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OwnerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ owner: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OwnerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OwnerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load owner on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.owner).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
