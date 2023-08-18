import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMatch, NewMatch } from '../match.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMatch for edit and NewMatchFormGroupInput for create.
 */
type MatchFormGroupInput = IMatch | PartialWithRequiredKeyOf<NewMatch>;

type MatchFormDefaults = Pick<NewMatch, 'id' | 'notifyMatch'>;

type MatchFormGroupContent = {
  id: FormControl<IMatch['id'] | NewMatch['id']>;
  notifyMatch: FormControl<IMatch['notifyMatch']>;
  dateMatch: FormControl<IMatch['dateMatch']>;
  contract: FormControl<IMatch['contract']>;
  firstLiked: FormControl<IMatch['firstLiked']>;
  secondLiked: FormControl<IMatch['secondLiked']>;
};

export type MatchFormGroup = FormGroup<MatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MatchFormService {
  createMatchFormGroup(match: MatchFormGroupInput = { id: null }): MatchFormGroup {
    const matchRawValue = {
      ...this.getFormDefaults(),
      ...match,
    };
    return new FormGroup<MatchFormGroupContent>({
      id: new FormControl(
        { value: matchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      notifyMatch: new FormControl(matchRawValue.notifyMatch),
      dateMatch: new FormControl(matchRawValue.dateMatch),
      contract: new FormControl(matchRawValue.contract),
      firstLiked: new FormControl(matchRawValue.firstLiked),
      secondLiked: new FormControl(matchRawValue.secondLiked),
    });
  }

  getMatch(form: MatchFormGroup): IMatch | NewMatch {
    return form.getRawValue() as IMatch | NewMatch;
  }

  resetForm(form: MatchFormGroup, match: MatchFormGroupInput): void {
    const matchRawValue = { ...this.getFormDefaults(), ...match };
    form.reset(
      {
        ...matchRawValue,
        id: { value: matchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MatchFormDefaults {
    return {
      id: null,
      notifyMatch: false,
    };
  }
}
