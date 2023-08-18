import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBreed, NewBreed } from '../breed.model';

export type PartialUpdateBreed = Partial<IBreed> & Pick<IBreed, 'id'>;

export type EntityResponseType = HttpResponse<IBreed>;
export type EntityArrayResponseType = HttpResponse<IBreed[]>;

@Injectable({ providedIn: 'root' })
export class BreedService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/breeds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(breed: NewBreed): Observable<EntityResponseType> {
    return this.http.post<IBreed>(this.resourceUrl, breed, { observe: 'response' });
  }

  update(breed: IBreed): Observable<EntityResponseType> {
    return this.http.put<IBreed>(`${this.resourceUrl}/${this.getBreedIdentifier(breed)}`, breed, { observe: 'response' });
  }

  partialUpdate(breed: PartialUpdateBreed): Observable<EntityResponseType> {
    return this.http.patch<IBreed>(`${this.resourceUrl}/${this.getBreedIdentifier(breed)}`, breed, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBreed>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBreed[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBreedIdentifier(breed: Pick<IBreed, 'id'>): number {
    return breed.id;
  }

  compareBreed(o1: Pick<IBreed, 'id'> | null, o2: Pick<IBreed, 'id'> | null): boolean {
    return o1 && o2 ? this.getBreedIdentifier(o1) === this.getBreedIdentifier(o2) : o1 === o2;
  }

  addBreedToCollectionIfMissing<Type extends Pick<IBreed, 'id'>>(
    breedCollection: Type[],
    ...breedsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const breeds: Type[] = breedsToCheck.filter(isPresent);
    if (breeds.length > 0) {
      const breedCollectionIdentifiers = breedCollection.map(breedItem => this.getBreedIdentifier(breedItem)!);
      const breedsToAdd = breeds.filter(breedItem => {
        const breedIdentifier = this.getBreedIdentifier(breedItem);
        if (breedCollectionIdentifiers.includes(breedIdentifier)) {
          return false;
        }
        breedCollectionIdentifiers.push(breedIdentifier);
        return true;
      });
      return [...breedsToAdd, ...breedCollection];
    }
    return breedCollection;
  }
}
