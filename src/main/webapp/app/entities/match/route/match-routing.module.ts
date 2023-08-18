import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatchComponent } from '../list/match.component';
import { MatchDetailComponent } from '../detail/match-detail.component';
import { MatchUpdateComponent } from '../update/match-update.component';
import { MatchRoutingResolveService } from './match-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const matchRoute: Routes = [
  {
    path: '',
    component: MatchComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MatchDetailComponent,
    resolve: {
      match: MatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MatchUpdateComponent,
    resolve: {
      match: MatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MatchUpdateComponent,
    resolve: {
      match: MatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matchRoute)],
  exports: [RouterModule],
})
export class MatchRoutingModule {}
