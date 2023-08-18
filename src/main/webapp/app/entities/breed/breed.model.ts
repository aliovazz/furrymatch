export interface IBreed {
  id: number;
  breed?: string | null;
  breedType?: string | null;
}

export type NewBreed = Omit<IBreed, 'id'> & { id: null };
