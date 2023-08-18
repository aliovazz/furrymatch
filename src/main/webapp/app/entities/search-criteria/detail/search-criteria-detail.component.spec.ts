import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SearchCriteriaDetailComponent } from './search-criteria-detail.component';

describe('SearchCriteria Management Detail Component', () => {
  let comp: SearchCriteriaDetailComponent;
  let fixture: ComponentFixture<SearchCriteriaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchCriteriaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ searchCriteria: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SearchCriteriaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SearchCriteriaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load searchCriteria on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.searchCriteria).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
