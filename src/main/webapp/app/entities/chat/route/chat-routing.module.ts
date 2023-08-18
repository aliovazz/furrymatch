import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChatComponent } from '../list/chat.component';
import { ChatDetailComponent } from '../detail/chat-detail.component';
import { ChatUpdateComponent } from '../update/chat-update.component';
import { ChatRoutingResolveService } from './chat-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const chatRoute: Routes = [
  {
    path: '',
    component: ChatComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChatDetailComponent,
    resolve: {
      chat: ChatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChatUpdateComponent,
    resolve: {
      chat: ChatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChatUpdateComponent,
    resolve: {
      chat: ChatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chatRoute)],
  exports: [RouterModule],
})
export class ChatRoutingModule {}
