import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SetListLineComponent } from '../list/set-list-line.component';
import { SetListLineDetailComponent } from '../detail/set-list-line-detail.component';
import { SetListLineUpdateComponent } from '../update/set-list-line-update.component';
import { SetListLineRoutingResolveService } from './set-list-line-routing-resolve.service';

const setListLineRoute: Routes = [
  {
    path: '',
    component: SetListLineComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SetListLineDetailComponent,
    resolve: {
      setListLine: SetListLineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SetListLineUpdateComponent,
    resolve: {
      setListLine: SetListLineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SetListLineUpdateComponent,
    resolve: {
      setListLine: SetListLineRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(setListLineRoute)],
  exports: [RouterModule],
})
export class SetListLineRoutingModule {}
