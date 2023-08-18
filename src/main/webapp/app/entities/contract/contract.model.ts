import dayjs from 'dayjs/esm';

export interface IContract {
  id: number;
  tradeMoney?: string | null;
  tradePups?: string | null;
  pedigree?: string | null;
  otherNotes?: string | null;
  contractDate?: dayjs.Dayjs | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
