import dayjs from 'dayjs/esm';
import { IMatch } from 'app/entities/match/match.model';

export interface IChat {
  id: number;
  dateChat?: dayjs.Dayjs | null;
  message?: string | null;
  stateChat?: string | null;
  match?: Pick<IMatch, 'id'> | null;
}

export type NewChat = Omit<IChat, 'id'> & { id: null };
