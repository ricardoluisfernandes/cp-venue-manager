import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'venue',
        data: { pageTitle: 'cpVenueManagerApp.venue.home.title' },
        loadChildren: () => import('./venue/venue.module').then(m => m.VenueModule),
      },
      {
        path: 'contractor',
        data: { pageTitle: 'cpVenueManagerApp.contractor.home.title' },
        loadChildren: () => import('./contractor/contractor.module').then(m => m.ContractorModule),
      },
      {
        path: 'set-list',
        data: { pageTitle: 'cpVenueManagerApp.setList.home.title' },
        loadChildren: () => import('./set-list/set-list.module').then(m => m.SetListModule),
      },
      {
        path: 'set-list-line',
        data: { pageTitle: 'cpVenueManagerApp.setListLine.home.title' },
        loadChildren: () => import('./set-list-line/set-list-line.module').then(m => m.SetListLineModule),
      },
      {
        path: 'song',
        data: { pageTitle: 'cpVenueManagerApp.song.home.title' },
        loadChildren: () => import('./song/song.module').then(m => m.SongModule),
      },
      {
        path: 'member',
        data: { pageTitle: 'cpVenueManagerApp.member.home.title' },
        loadChildren: () => import('./member/member.module').then(m => m.MemberModule),
      },
      {
        path: 'member-availability',
        data: { pageTitle: 'cpVenueManagerApp.memberAvailability.home.title' },
        loadChildren: () => import('./member-availability/member-availability.module').then(m => m.MemberAvailabilityModule),
      },
      {
        path: 'expense',
        data: { pageTitle: 'cpVenueManagerApp.expense.home.title' },
        loadChildren: () => import('./expense/expense.module').then(m => m.ExpenseModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'cpVenueManagerApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
