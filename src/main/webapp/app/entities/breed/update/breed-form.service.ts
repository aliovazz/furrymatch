import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBreed, NewBreed } from '../breed.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBreed for edit and NewBreedFormGroupInput for create.
 */
type BreedFormGroupInput = IBreed | PartialWithRequiredKeyOf<NewBreed>;

type BreedFormDefaults = Pick<NewBreed, 'id'>;

type BreedFormGroupContent = {
  id: FormControl<IBreed['id'] | NewBreed['id']>;
  breed: FormControl<IBreed['breed']>;
  breedType: FormControl<IBreed['breedType']>;
};

export type BreedFormGroup = FormGroup<BreedFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BreedFormService {
  createBreedFormGroup(breed: BreedFormGroupInput = { id: null }): BreedFormGroup {
    const breedRawValue = {
      ...this.getFormDefaults(),
      ...breed,
    };
    return new FormGroup<BreedFormGroupContent>({
      id: new FormControl(
        { value: breedRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      breed: new FormControl(breedRawValue.breed, {
        validators: [Validators.required],
      }),
      breedType: new FormControl(breedRawValue.breedType, {
        validators: [Validators.required],
      }),
    });
  }

  getBreed(form: BreedFormGroup): IBreed | NewBreed {
    return form.getRawValue() as IBreed | NewBreed;
  }

  resetForm(form: BreedFormGroup, breed: BreedFormGroupInput): void {
    const breedRawValue = { ...this.getFormDefaults(), ...breed };
    form.reset(
      {
        ...breedRawValue,
        id: { value: breedRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BreedFormDefaults {
    return {
      id: null,
    };
  }
}
