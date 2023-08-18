import { Sex } from 'app/entities/enumerations/sex.model';

import { ISearchCriteria, NewSearchCriteria } from './search-criteria.model';

export const sampleWithRequiredData: ISearchCriteria = {
  id: 64881,
  filterType: 'deposit Arkansas',
};

export const sampleWithPartialData: ISearchCriteria = {
  id: 44974,
  filterType: 'haptic',
  breed: 'quantify e-markets',
  sex: Sex['Macho'],
  pedigree: 'Berkshire firmware 24/7',
  tradeMoney: 'Future optimal',
  canton: 'Open-source haptic',
  district: 'HTTP',
  objective: 'Bedfordshire',
};

export const sampleWithFullData: ISearchCriteria = {
  id: 5569,
  filterType: 'RAM Principal',
  breed: 'architectures impactful',
  tradePups: 'transmit',
  sex: Sex['Hembra'],
  pedigree: 'Ukraine CFA',
  tradeMoney: 'THX Pizza withdrawal',
  provice: 'generating Buckinghamshire',
  canton: 'Facilitator Savings background',
  district: 'Sausages Jewelery',
  objective: 'Club multi-byte Central',
};

export const sampleWithNewData: NewSearchCriteria = {
  filterType: 'Diverse',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
