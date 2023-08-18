import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../search-criteria.test-samples';

import { SearchCriteriaFormService } from './search-criteria-form.service';

describe('SearchCriteria Form Service', () => {
  let service: SearchCriteriaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchCriteriaFormService);
  });

  describe('Service methods', () => {
    describe('createSearchCriteriaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSearchCriteriaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filterType: expect.any(Object),
            breed: expect.any(Object),
            tradePups: expect.any(Object),
            sex: expect.any(Object),
            pedigree: expect.any(Object),
            tradeMoney: expect.any(Object),
            provice: expect.any(Object),
            canton: expect.any(Object),
            district: expect.any(Object),
            objective: expect.any(Object),
            pet: expect.any(Object),
          })
        );
      });

      it('passing ISearchCriteria should create a new form with FormGroup', () => {
        const formGroup = service.createSearchCriteriaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filterType: expect.any(Object),
            breed: expect.any(Object),
            tradePups: expect.any(Object),
            sex: expect.any(Object),
            pedigree: expect.any(Object),
            tradeMoney: expect.any(Object),
            provice: expect.any(Object),
            canton: expect.any(Object),
            district: expect.any(Object),
            objective: expect.any(Object),
            pet: expect.any(Object),
          })
        );
      });
    });

    describe('getSearchCriteria', () => {
      it('should return NewSearchCriteria for default SearchCriteria initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSearchCriteriaFormGroup(sampleWithNewData);

        const searchCriteria = service.getSearchCriteria(formGroup) as any;

        expect(searchCriteria).toMatchObject(sampleWithNewData);
      });

      it('should return NewSearchCriteria for empty SearchCriteria initial value', () => {
        const formGroup = service.createSearchCriteriaFormGroup();

        const searchCriteria = service.getSearchCriteria(formGroup) as any;

        expect(searchCriteria).toMatchObject({});
      });

      it('should return ISearchCriteria', () => {
        const formGroup = service.createSearchCriteriaFormGroup(sampleWithRequiredData);

        const searchCriteria = service.getSearchCriteria(formGroup) as any;

        expect(searchCriteria).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISearchCriteria should not enable id FormControl', () => {
        const formGroup = service.createSearchCriteriaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSearchCriteria should disable id FormControl', () => {
        const formGroup = service.createSearchCriteriaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
