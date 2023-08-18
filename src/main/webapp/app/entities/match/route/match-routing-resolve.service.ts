import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMatch } from '../match.model';
import { MatchService } from '../service/match.service';

@Injectable({ providedIn: 'root' })
export class MatchRoutingResolveService implements Resolve<IMatch | null> {
  constructor(protected service: MatchService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMatch | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((match: HttpResponse<IMatch>) => {
          if (match.body) {
            return of(match.body);
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
