import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISearchCriteria, NewSearchCriteria } from '../search-criteria.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISearchCriteria for edit and NewSearchCriteriaFormGroupInput for create.
 */
type SearchCriteriaFormGroupInput = ISearchCriteria | PartialWithRequiredKeyOf<NewSearchCriteria>;

type SearchCriteriaFormDefaults = Pick<NewSearchCriteria, 'id'>;

type SearchCriteriaFormGroupContent = {
  id: FormControl<ISearchCriteria['id'] | NewSearchCriteria['id']>;
  filterType: FormControl<ISearchCriteria['filterType']>;
  breed: FormControl<ISearchCriteria['breed']>;
  tradePups: FormControl<ISearchCriteria['tradePups']>;
  sex: FormControl<ISearchCriteria['sex']>;
  pedigree: FormControl<ISearchCriteria['pedigree']>;
  tradeMoney: FormControl<ISearchCriteria['tradeMoney']>;
  provice: FormControl<ISearchCriteria['provice']>;
  canton: FormControl<ISearchCriteria['canton']>;
  district: FormControl<ISearchCriteria['district']>;
  objective: FormControl<ISearchCriteria['objective']>;
  pet: FormControl<ISearchCriteria['pet']>;
};

export type SearchCriteriaFormGroup = FormGroup<SearchCriteriaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SearchCriteriaFormService {
  createSearchCriteriaFormGroup(searchCriteria: SearchCriteriaFormGroupInput = { id: null }): SearchCriteriaFormGroup {
    const searchCriteriaRawValue = {
      ...this.getFormDefaults(),
      ...searchCriteria,
    };
    return new FormGroup<SearchCriteriaFormGroupContent>({
      id: new FormControl(
        { value: searchCriteriaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      filterType: new FormControl(searchCriteriaRawValue.filterType, {
        validators: [Validators.required],
      }),
      breed: new FormControl(searchCriteriaRawValue.breed),
      tradePups: new FormControl(searchCriteriaRawValue.tradePups),
      sex: new FormControl(searchCriteriaRawValue.sex),
      pedigree: new FormControl(searchCriteriaRawValue.pedigree),
      tradeMoney: new FormControl(searchCriteriaRawValue.tradeMoney),
      provice: new FormControl(searchCriteriaRawValue.provice),
      canton: new FormControl(searchCriteriaRawValue.canton),
      district: new FormControl(searchCriteriaRawValue.district),
      objective: new FormControl(searchCriteriaRawValue.objective),
      pet: new FormControl(searchCriteriaRawValue.pet),
    });
  }

  getSearchCriteria(form: SearchCriteriaFormGroup): ISearchCriteria | NewSearchCriteria {
    return form.getRawValue() as ISearchCriteria | NewSearchCriteria;
  }

  resetForm(form: SearchCriteriaFormGroup, searchCriteria: SearchCriteriaFormGroupInput): void {
    const searchCriteriaRawValue = { ...this.getFormDefaults(), ...searchCriteria };
    form.reset(
      {
        ...searchCriteriaRawValue,
        id: { value: searchCriteriaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SearchCriteriaFormDefaults {
    return {
      id: null,
    };
  }
}
