import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SearchCriteriaComponent } from '../list/search-criteria.component';
import { SearchCriteriaDetailComponent } from '../detail/search-criteria-detail.component';
import { SearchCriteriaUpdateComponent } from '../update/search-criteria-update.component';
import { SearchCriteriaRoutingResolveService } from './search-criteria-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const searchCriteriaRoute: Routes = [
  {
    path: '',
    component: SearchCriteriaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SearchCriteriaDetailComponent,
    resolve: {
      searchCriteria: SearchCriteriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SearchCriteriaUpdateComponent,
    resolve: {
      searchCriteria: SearchCriteriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SearchCriteriaUpdateComponent,
    resolve: {
      searchCriteria: SearchCriteriaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(searchCriteriaRoute)],
  exports: [RouterModule],
})
export class SearchCriteriaRoutingModule {}
