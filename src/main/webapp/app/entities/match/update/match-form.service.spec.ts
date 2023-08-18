import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../match.test-samples';

import { MatchFormService } from './match-form.service';

describe('Match Form Service', () => {
  let service: MatchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatchFormService);
  });

  describe('Service methods', () => {
    describe('createMatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            notifyMatch: expect.any(Object),
            dateMatch: expect.any(Object),
            contract: expect.any(Object),
            firstLiked: expect.any(Object),
            secondLiked: expect.any(Object),
          })
        );
      });

      it('passing IMatch should create a new form with FormGroup', () => {
        const formGroup = service.createMatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            notifyMatch: expect.any(Object),
            dateMatch: expect.any(Object),
            contract: expect.any(Object),
            firstLiked: expect.any(Object),
            secondLiked: expect.any(Object),
          })
        );
      });
    });

    describe('getMatch', () => {
      it('should return NewMatch for default Match initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMatchFormGroup(sampleWithNewData);

        const match = service.getMatch(formGroup) as any;

        expect(match).toMatchObject(sampleWithNewData);
      });

      it('should return NewMatch for empty Match initial value', () => {
        const formGroup = service.createMatchFormGroup();

        const match = service.getMatch(formGroup) as any;

        expect(match).toMatchObject({});
      });

      it('should return IMatch', () => {
        const formGroup = service.createMatchFormGroup(sampleWithRequiredData);

        const match = service.getMatch(formGroup) as any;

        expect(match).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMatch should not enable id FormControl', () => {
        const formGroup = service.createMatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMatch should disable id FormControl', () => {
        const formGroup = service.createMatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
