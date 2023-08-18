import { IPet } from 'app/entities/pet/pet.model';
import { LikeType } from 'app/entities/enumerations/like-type.model';

export interface ILikee {
  id: number;
  likeState?: LikeType | null;
  firstPet?: Pick<IPet, 'id'> | null;
  secondPet?: Pick<IPet, 'id'> | null;
}

export type NewLikee = Omit<ILikee, 'id'> & { id: null };
