import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChat } from '../chat.model';
import { ChatService } from '../service/chat.service';

@Injectable({ providedIn: 'root' })
export class ChatRoutingResolveService implements Resolve<IChat | null> {
  constructor(protected service: ChatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChat | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chat: HttpResponse<IChat>) => {
          if (chat.body) {
            return of(chat.body);
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
