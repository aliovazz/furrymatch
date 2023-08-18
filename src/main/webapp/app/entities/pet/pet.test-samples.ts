import { PetType } from 'app/entities/enumerations/pet-type.model';
import { Sex } from 'app/entities/enumerations/sex.model';

import { IPet, NewPet } from './pet.model';

export const sampleWithRequiredData: IPet = {
  id: 62498,
  name: 'Legacy',
  petType: PetType['Gato'],
  description: 'COM invoice',
  sex: Sex['Macho'],
};

export const sampleWithPartialData: IPet = {
  id: 78891,
  name: 'Pizza Shoes',
  petType: PetType['Perro'],
  description: 'EXE Wooden Technician',
  sex: Sex['Macho'],
  tradeMoney: false,
  tradePups: false,
  pedigree: false,
  desireAmmount: 24216,
};

export const sampleWithFullData: IPet = {
  id: 9780,
  name: 'efficient Account',
  petType: PetType['Perro'],
  description: 'Avon parse',
  sex: Sex['Macho'],
  tradeMoney: false,
  tradePups: false,
  pedigree: true,
  desireAmmount: 31787,
};

export const sampleWithNewData: NewPet = {
  name: 'bluetooth magenta Identity',
  petType: PetType['Perro'],
  description: 'Digitized',
  sex: Sex['Hembra'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
