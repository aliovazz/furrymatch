import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatchComponent } from './list/match.component';
import { MatchDetailComponent } from './detail/match-detail.component';
import { MatchUpdateComponent } from './update/match-update.component';
import { MatchDeleteDialogComponent } from './delete/match-delete-dialog.component';
import { MatchRoutingModule } from './route/match-routing.module';

@NgModule({
  imports: [SharedModule, MatchRoutingModule],
  declarations: [MatchComponent, MatchDetailComponent, MatchUpdateComponent, MatchDeleteDialogComponent],
})
export class MatchModule {}
