import dayjs from 'dayjs/esm';

import { IMatch, NewMatch } from './match.model';

export const sampleWithRequiredData: IMatch = {
  id: 82797,
};

export const sampleWithPartialData: IMatch = {
  id: 88472,
  notifyMatch: true,
};

export const sampleWithFullData: IMatch = {
  id: 727,
  notifyMatch: true,
  dateMatch: dayjs('2023-03-22'),
};

export const sampleWithNewData: NewMatch = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
