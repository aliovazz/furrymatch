import { IBreed, NewBreed } from './breed.model';

export const sampleWithRequiredData: IBreed = {
  id: 61893,
  breed: 'Soap B2B Account',
  breedType: 'Peso Configuration',
};

export const sampleWithPartialData: IBreed = {
  id: 27026,
  breed: 'synthesizing',
  breedType: 'web-enabled Checking Naira',
};

export const sampleWithFullData: IBreed = {
  id: 5899,
  breed: 'invoice payment',
  breedType: 'deliver',
};

export const sampleWithNewData: NewBreed = {
  breed: 'infomediaries',
  breedType: 'Granite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
