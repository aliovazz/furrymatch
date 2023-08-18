import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../breed.test-samples';

import { BreedFormService } from './breed-form.service';

describe('Breed Form Service', () => {
  let service: BreedFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BreedFormService);
  });

  describe('Service methods', () => {
    describe('createBreedFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBreedFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            breed: expect.any(Object),
            breedType: expect.any(Object),
          })
        );
      });

      it('passing IBreed should create a new form with FormGroup', () => {
        const formGroup = service.createBreedFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            breed: expect.any(Object),
            breedType: expect.any(Object),
          })
        );
      });
    });

    describe('getBreed', () => {
      it('should return NewBreed for default Breed initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBreedFormGroup(sampleWithNewData);

        const breed = service.getBreed(formGroup) as any;

        expect(breed).toMatchObject(sampleWithNewData);
      });

      it('should return NewBreed for empty Breed initial value', () => {
        const formGroup = service.createBreedFormGroup();

        const breed = service.getBreed(formGroup) as any;

        expect(breed).toMatchObject({});
      });

      it('should return IBreed', () => {
        const formGroup = service.createBreedFormGroup(sampleWithRequiredData);

        const breed = service.getBreed(formGroup) as any;

        expect(breed).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBreed should not enable id FormControl', () => {
        const formGroup = service.createBreedFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBreed should disable id FormControl', () => {
        const formGroup = service.createBreedFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
