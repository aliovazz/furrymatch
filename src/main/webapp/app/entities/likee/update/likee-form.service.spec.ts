import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../likee.test-samples';

import { LikeeFormService } from './likee-form.service';

describe('Likee Form Service', () => {
  let service: LikeeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LikeeFormService);
  });

  describe('Service methods', () => {
    describe('createLikeeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLikeeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            likeState: expect.any(Object),
            firstPet: expect.any(Object),
            secondPet: expect.any(Object),
          })
        );
      });

      it('passing ILikee should create a new form with FormGroup', () => {
        const formGroup = service.createLikeeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            likeState: expect.any(Object),
            firstPet: expect.any(Object),
            secondPet: expect.any(Object),
          })
        );
      });
    });

    describe('getLikee', () => {
      it('should return NewLikee for default Likee initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLikeeFormGroup(sampleWithNewData);

        const likee = service.getLikee(formGroup) as any;

        expect(likee).toMatchObject(sampleWithNewData);
      });

      it('should return NewLikee for empty Likee initial value', () => {
        const formGroup = service.createLikeeFormGroup();

        const likee = service.getLikee(formGroup) as any;

        expect(likee).toMatchObject({});
      });

      it('should return ILikee', () => {
        const formGroup = service.createLikeeFormGroup(sampleWithRequiredData);

        const likee = service.getLikee(formGroup) as any;

        expect(likee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILikee should not enable id FormControl', () => {
        const formGroup = service.createLikeeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLikee should disable id FormControl', () => {
        const formGroup = service.createLikeeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
