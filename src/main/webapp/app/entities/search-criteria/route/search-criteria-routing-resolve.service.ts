import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISearchCriteria } from '../search-criteria.model';
import { SearchCriteriaService } from '../service/search-criteria.service';

@Injectable({ providedIn: 'root' })
export class SearchCriteriaRoutingResolveService implements Resolve<ISearchCriteria | null> {
  constructor(protected service: SearchCriteriaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISearchCriteria | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((searchCriteria: HttpResponse<ISearchCriteria>) => {
          if (searchCriteria.body) {
            return of(searchCriteria.body);
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
