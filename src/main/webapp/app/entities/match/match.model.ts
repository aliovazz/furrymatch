import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/contract/contract.model';
import { ILikee } from 'app/entities/likee/likee.model';

export interface IMatch {
  id: number;
  notifyMatch?: boolean | null;
  dateMatch?: dayjs.Dayjs | null;
  contract?: Pick<IContract, 'id'> | null;
  firstLiked?: Pick<ILikee, 'id'> | null;
  secondLiked?: Pick<ILikee, 'id'> | null;
}

export type NewMatch = Omit<IMatch, 'id'> & { id: null };
