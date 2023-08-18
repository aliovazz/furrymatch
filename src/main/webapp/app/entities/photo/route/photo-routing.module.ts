import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PhotoComponent } from '../list/photo.component';
import { PhotoDetailComponent } from '../detail/photo-detail.component';
import { PhotoUpdateComponent } from '../update/photo-update.component';
import { PhotoRoutingResolveService } from './photo-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const photoRoute: Routes = [
  {
    path: '',
    component: PhotoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PhotoDetailComponent,
    resolve: {
      photo: PhotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PhotoUpdateComponent,
    resolve: {
      photo: PhotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PhotoUpdateComponent,
    resolve: {
      photo: PhotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(photoRoute)],
  exports: [RouterModule],
})
export class PhotoRoutingModule {}
