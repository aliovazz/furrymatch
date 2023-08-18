import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'owner',
        data: { pageTitle: 'furryMatchApp.owner.home.title' },
        loadChildren: () => import('./owner/owner.module').then(m => m.OwnerModule),
      },
      {
        path: 'pet',
        data: { pageTitle: 'furryMatchApp.pet.home.title' },
        loadChildren: () => import('./pet/pet.module').then(m => m.PetModule),
      },
      {
        path: 'photo',
        data: { pageTitle: 'furryMatchApp.photo.home.title' },
        loadChildren: () => import('./photo/photo.module').then(m => m.PhotoModule),
      },
      {
        path: 'breed',
        data: { pageTitle: 'furryMatchApp.breed.home.title' },
        loadChildren: () => import('./breed/breed.module').then(m => m.BreedModule),
      },
      {
        path: 'search-criteria',
        data: { pageTitle: 'furryMatchApp.searchCriteria.home.title' },
        loadChildren: () => import('./search-criteria/search-criteria.module').then(m => m.SearchCriteriaModule),
      },
      {
        path: 'likee',
        data: { pageTitle: 'furryMatchApp.likee.home.title' },
        loadChildren: () => import('./likee/likee.module').then(m => m.LikeeModule),
      },
      {
        path: 'match',
        data: { pageTitle: 'furryMatchApp.match.home.title' },
        loadChildren: () => import('./match/match.module').then(m => m.MatchModule),
      },
      {
        path: 'contract',
        data: { pageTitle: 'furryMatchApp.contract.home.title' },
        loadChildren: () => import('./contract/contract.module').then(m => m.ContractModule),
      },
      {
        path: 'chat',
        data: { pageTitle: 'furryMatchApp.chat.home.title' },
        loadChildren: () => import('./chat/chat.module').then(m => m.ChatModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
