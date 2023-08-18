import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PhotoDetailComponent } from './photo-detail.component';

describe('Photo Management Detail Component', () => {
  let comp: PhotoDetailComponent;
  let fixture: ComponentFixture<PhotoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PhotoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ photo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PhotoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PhotoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load photo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.photo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
