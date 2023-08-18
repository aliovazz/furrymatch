import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LikeeComponent } from '../list/likee.component';
import { LikeeDetailComponent } from '../detail/likee-detail.component';
import { LikeeUpdateComponent } from '../update/likee-update.component';
import { LikeeRoutingResolveService } from './likee-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const likeeRoute: Routes = [
  {
    path: '',
    component: LikeeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LikeeDetailComponent,
    resolve: {
      likee: LikeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LikeeUpdateComponent,
    resolve: {
      likee: LikeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LikeeUpdateComponent,
    resolve: {
      likee: LikeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(likeeRoute)],
  exports: [RouterModule],
})
export class LikeeRoutingModule {}
