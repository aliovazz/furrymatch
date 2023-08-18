import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBreed } from '../breed.model';
import { BreedService } from '../service/breed.service';

@Injectable({ providedIn: 'root' })
export class BreedRoutingResolveService implements Resolve<IBreed | null> {
  constructor(protected service: BreedService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBreed | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((breed: HttpResponse<IBreed>) => {
          if (breed.body) {
            return of(breed.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
