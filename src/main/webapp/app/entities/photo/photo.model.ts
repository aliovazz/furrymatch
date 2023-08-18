import dayjs from 'dayjs/esm';
import { IPet } from 'app/entities/pet/pet.model';

export interface IPhoto {
  id: number;
  uploadDate?: dayjs.Dayjs | null;
  photoUrl?: string | null;
  pet?: Pick<IPet, 'id'> | null;
  customId?: string;
}

export type NewPhoto = Omit<IPhoto, 'id'> & { id: null };
