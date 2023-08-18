import dayjs from 'dayjs/esm';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 90219,
};

export const sampleWithPartialData: IContract = {
  id: 35474,
  tradeMoney: 'lavender',
  pedigree: 'Harbor Supervisor',
  contractDate: dayjs('2023-03-22'),
};

export const sampleWithFullData: IContract = {
  id: 98438,
  tradeMoney: 'generating Rustic Texas',
  tradePups: 'to one-to-one',
  pedigree: 'definition Handmade',
  otherNotes: 'emulation Ergonomic',
  contractDate: dayjs('2023-03-22'),
};

export const sampleWithNewData: NewContract = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
