import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MemberAvailabilityComponent } from '../list/member-availability.component';
import { MemberAvailabilityDetailComponent } from '../detail/member-availability-detail.component';
import { MemberAvailabilityUpdateComponent } from '../update/member-availability-update.component';
import { MemberAvailabilityRoutingResolveService } from './member-availability-routing-resolve.service';

const memberAvailabilityRoute: Routes = [
  {
    path: '',
    component: MemberAvailabilityComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MemberAvailabilityDetailComponent,
    resolve: {
      memberAvailability: MemberAvailabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MemberAvailabilityUpdateComponent,
    resolve: {
      memberAvailability: MemberAvailabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MemberAvailabilityUpdateComponent,
    resolve: {
      memberAvailability: MemberAvailabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(memberAvailabilityRoute)],
  exports: [RouterModule],
})
export class MemberAvailabilityRoutingModule {}
