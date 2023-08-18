import { IPet } from 'app/entities/pet/pet.model';
import { Sex } from 'app/entities/enumerations/sex.model';

export interface ISearchCriteria {
  id: number;
  filterType?: string | null;
  breed?: string | null;
  tradePups?: string | null;
  sex?: Sex | null;
  pedigree?: string | null;
  tradeMoney?: string | null;
  provice?: string | null;
  canton?: string | null;
  district?: string | null;
  objective?: string | null;
  pet?: Pick<IPet, 'id'> | null;
}

export type NewSearchCriteria = Omit<ISearchCriteria, 'id'> & { id: null };
