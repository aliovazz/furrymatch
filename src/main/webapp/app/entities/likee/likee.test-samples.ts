import { LikeType } from 'app/entities/enumerations/like-type.model';

import { ILikee, NewLikee } from './likee.model';

export const sampleWithRequiredData: ILikee = {
  id: 56354,
  likeState: LikeType['Like'],
};

export const sampleWithPartialData: ILikee = {
  id: 4265,
  likeState: LikeType['Dislike'],
};

export const sampleWithFullData: ILikee = {
  id: 93882,
  likeState: LikeType['Dislike'],
};

export const sampleWithNewData: NewLikee = {
  likeState: LikeType['Like'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
