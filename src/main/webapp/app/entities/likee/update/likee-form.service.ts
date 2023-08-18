import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILikee, NewLikee } from '../likee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILikee for edit and NewLikeeFormGroupInput for create.
 */
type LikeeFormGroupInput = ILikee | PartialWithRequiredKeyOf<NewLikee>;

type LikeeFormDefaults = Pick<NewLikee, 'id'>;

type LikeeFormGroupContent = {
  id: FormControl<ILikee['id'] | NewLikee['id']>;
  likeState: FormControl<ILikee['likeState']>;
  firstPet: FormControl<ILikee['firstPet']>;
  secondPet: FormControl<ILikee['secondPet']>;
};

export type LikeeFormGroup = FormGroup<LikeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LikeeFormService {
  createLikeeFormGroup(likee: LikeeFormGroupInput = { id: null }): LikeeFormGroup {
    const likeeRawValue = {
      ...this.getFormDefaults(),
      ...likee,
    };
    return new FormGroup<LikeeFormGroupContent>({
      id: new FormControl(
        { value: likeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      likeState: new FormControl(likeeRawValue.likeState, {
        validators: [Validators.required],
      }),
      firstPet: new FormControl(likeeRawValue.firstPet),
      secondPet: new FormControl(likeeRawValue.secondPet),
    });
  }

  getLikee(form: LikeeFormGroup): ILikee | NewLikee {
    return form.getRawValue() as ILikee | NewLikee;
  }

  resetForm(form: LikeeFormGroup, likee: LikeeFormGroupInput): void {
    const likeeRawValue = { ...this.getFormDefaults(), ...likee };
    form.reset(
      {
        ...likeeRawValue,
        id: { value: likeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LikeeFormDefaults {
    return {
      id: null,
    };
  }
}
