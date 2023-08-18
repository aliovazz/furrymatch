import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OwnerComponent } from '../list/owner.component';
import { OwnerDetailComponent } from '../detail/owner-detail.component';
import { OwnerUpdateComponent } from '../update/owner-update.component';
import { OwnerRoutingResolveService } from './owner-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ownerRoute: Routes = [
  {
    path: '',
    component: OwnerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OwnerDetailComponent,
    resolve: {
      owner: OwnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OwnerUpdateComponent,
    resolve: {
      owner: OwnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OwnerUpdateComponent,
    resolve: {
      owner: OwnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ownerRoute)],
  exports: [RouterModule],
})
export class OwnerRoutingModule {}
