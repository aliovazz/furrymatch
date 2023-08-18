import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../owner.test-samples';

import { OwnerFormService } from './owner-form.service';

describe('Owner Form Service', () => {
  let service: OwnerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OwnerFormService);
  });

  describe('Service methods', () => {
    describe('createOwnerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOwnerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            user_id: expect.any(Object),
            firstName: expect.any(Object),
            secondName: expect.any(Object),
            firstLastName: expect.any(Object),
            secondLastName: expect.any(Object),
            phoneNumber: expect.any(Object),
            photo: expect.any(Object),
            identityNumber: expect.any(Object),
            address: expect.any(Object),
            province: expect.any(Object),
            canton: expect.any(Object),
            district: expect.any(Object),
            otp: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IOwner should create a new form with FormGroup', () => {
        const formGroup = service.createOwnerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            user_id: expect.any(Object),
            firstName: expect.any(Object),
            secondName: expect.any(Object),
            firstLastName: expect.any(Object),
            secondLastName: expect.any(Object),
            phoneNumber: expect.any(Object),
            photo: expect.any(Object),
            identityNumber: expect.any(Object),
            address: expect.any(Object),
            province: expect.any(Object),
            canton: expect.any(Object),
            district: expect.any(Object),
            otp: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getOwner', () => {
      it('should return NewOwner for default Owner initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOwnerFormGroup(sampleWithNewData);

        const owner = service.getOwner(formGroup) as any;

        expect(owner).toMatchObject(sampleWithNewData);
      });

      it('should return NewOwner for empty Owner initial value', () => {
        const formGroup = service.createOwnerFormGroup();

        const owner = service.getOwner(formGroup) as any;

        expect(owner).toMatchObject({});
      });

      it('should return IOwner', () => {
        const formGroup = service.createOwnerFormGroup(sampleWithRequiredData);

        const owner = service.getOwner(formGroup) as any;

        expect(owner).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOwner should not enable id FormControl', () => {
        const formGroup = service.createOwnerFormGroup();
        expect(formGroup.controls.user_id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.user_id.disabled).toBe(true);
      });

      it('passing NewOwner should disable id FormControl', () => {
        const formGroup = service.createOwnerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.user_id.disabled).toBe(true);

        service.resetForm(formGroup, { user_id: null });

        expect(formGroup.controls.user_id.disabled).toBe(true);
      });
    });
  });
});
