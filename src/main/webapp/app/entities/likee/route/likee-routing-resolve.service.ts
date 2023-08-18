import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILikee } from '../likee.model';
import { LikeeService } from '../service/likee.service';

@Injectable({ providedIn: 'root' })
export class LikeeRoutingResolveService implements Resolve<ILikee | null> {
  constructor(protected service: LikeeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILikee | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((likee: HttpResponse<ILikee>) => {
          if (likee.body) {
            return of(likee.body);
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
