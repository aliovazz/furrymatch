import dayjs from 'dayjs/esm';

import { IOwner, NewOwner } from './owner.model';

export const sampleWithRequiredData: IOwner = {
  user_id: 70414,
  firstName: 'Manley',
  firstLastName: 'syndicate',
  secondLastName: 'Markets compressing',
  phoneNumber: 11916,
  identityNumber: 'FTP GB',
  address: 'Kids',
  province: 'Estonia',
  canton: 'Human convergence',
  district: 'analyzing Loan',
};

export const sampleWithPartialData: IOwner = {
  user_id: 2445,
  firstName: 'Ernest',
  firstLastName: 'Stravenue Granite world-class',
  secondLastName: 'Computers',
  phoneNumber: 61149,
  identityNumber: 'Gorgeous Dynamic',
  address: 'Honduras Leu indexing',
  province: 'transmitter',
  canton: 'Bike',
  district: 'Small',
};

export const sampleWithFullData: IOwner = {
  user_id: 70225,
  firstName: 'Tiana',
  secondName: 'deliverables Investor maroon',
  firstLastName: 'cohesive',
  secondLastName: 'Carolina invoice primary',
  phoneNumber: 77489,
  photo: 'plum',
  identityNumber: 'radical',
  address: 'Operative JBOD invoice',
  province: 'Avon Solutions',
  canton: 'hacking generate',
  district: 'Product index',
  otp: 'Account South Generic',
  createdAt: dayjs('2023-03-22'),
  updatedAt: dayjs('2023-03-22'),
};

export const sampleWithNewData: NewOwner = {
  firstName: 'Grover',
  firstLastName: 'cross-platform Movies',
  secondLastName: 'Soft Hat Metrics',
  phoneNumber: 7397,
  identityNumber: 'matrix',
  address: 'Kyrgyz',
  province: 'Loan bleeding-edge',
  canton: 'programming',
  district: 'International eyeballs transmit',
  user_id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
