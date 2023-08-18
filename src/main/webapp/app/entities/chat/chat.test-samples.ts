import dayjs from 'dayjs/esm';

import { IChat, NewChat } from './chat.model';

export const sampleWithRequiredData: IChat = {
  id: 24892,
};

export const sampleWithPartialData: IChat = {
  id: 84850,
};

export const sampleWithFullData: IChat = {
  id: 6002,
  dateChat: dayjs('2023-03-22'),
  message: 'matrix Steel program',
  stateChat: 'Towels National complexity',
};

export const sampleWithNewData: NewChat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
