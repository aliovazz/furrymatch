import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOwner, NewOwner } from '../owner.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { user_id: unknown }> = Partial<Omit<T, 'user_id'>> & { user_id: T['user_id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOwner for edit and NewOwnerFormGroupInput for create.
 */
type OwnerFormGroupInput = IOwner | PartialWithRequiredKeyOf<NewOwner>;

type OwnerFormDefaults = Pick<NewOwner, 'user_id'>;

type OwnerFormGroupContent = {
  user_id: FormControl<IOwner['user_id'] | NewOwner['user_id']>;
  firstName: FormControl<IOwner['firstName']>;
  secondName: FormControl<IOwner['secondName']>;
  firstLastName: FormControl<IOwner['firstLastName']>;
  secondLastName: FormControl<IOwner['secondLastName']>;
  phoneNumber: FormControl<IOwner['phoneNumber']>;
  photo: FormControl<IOwner['photo']>;
  identityNumber: FormControl<IOwner['identityNumber']>;
  address: FormControl<IOwner['address']>;
  province: FormControl<IOwner['province']>;
  canton: FormControl<IOwner['canton']>;
  district: FormControl<IOwner['district']>;
  otp: FormControl<IOwner['otp']>;
  createdAt: FormControl<IOwner['createdAt']>;
  updatedAt: FormControl<IOwner['updatedAt']>;
  user: FormControl<IOwner['user']>;
};

export type OwnerFormGroup = FormGroup<OwnerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OwnerFormService {
  createOwnerFormGroup(owner: OwnerFormGroupInput = { user_id: null }): OwnerFormGroup {
    const ownerRawValue = {
      ...this.getFormDefaults(),
      ...owner,
    };
    return new FormGroup<OwnerFormGroupContent>({
      user_id: new FormControl(
        { value: ownerRawValue.user_id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(ownerRawValue.firstName, {
        validators: [Validators.required],
      }),
      secondName: new FormControl(ownerRawValue.secondName),
      firstLastName: new FormControl(ownerRawValue.firstLastName, {
        validators: [Validators.required],
      }),
      secondLastName: new FormControl(ownerRawValue.secondLastName, {
        validators: [Validators.required],
      }),
      phoneNumber: new FormControl(ownerRawValue.phoneNumber, {
        validators: [Validators.required],
      }),
      photo: new FormControl(ownerRawValue.photo),
      identityNumber: new FormControl(ownerRawValue.identityNumber, {
        validators: [Validators.required],
      }),
      address: new FormControl(ownerRawValue.address, {
        validators: [Validators.required],
      }),
      province: new FormControl(ownerRawValue.province, {
        validators: [Validators.required],
      }),
      canton: new FormControl(ownerRawValue.canton, {
        validators: [Validators.required],
      }),
      district: new FormControl(ownerRawValue.district, {
        validators: [Validators.required],
      }),
      otp: new FormControl(ownerRawValue.otp),
      createdAt: new FormControl(ownerRawValue.createdAt),
      updatedAt: new FormControl(ownerRawValue.updatedAt),
      user: new FormControl(ownerRawValue.user),
    });
  }

  getOwner(form: OwnerFormGroup): IOwner | NewOwner {
    return form.getRawValue() as IOwner | NewOwner;
  }

  resetForm(form: OwnerFormGroup, owner: OwnerFormGroupInput): void {
    const ownerRawValue = { ...this.getFormDefaults(), ...owner };
    form.reset(
      {
        ...ownerRawValue,
        id: { value: ownerRawValue.user_id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OwnerFormDefaults {
    return {
      user_id: null,
    };
  }
}
