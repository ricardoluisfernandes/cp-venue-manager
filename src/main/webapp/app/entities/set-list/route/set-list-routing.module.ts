import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SetListComponent } from '../list/set-list.component';
import { SetListDetailComponent } from '../detail/set-list-detail.component';
import { SetListUpdateComponent } from '../update/set-list-update.component';
import { SetListRoutingResolveService } from './set-list-routing-resolve.service';

const setListRoute: Routes = [
  {
    path: '',
    component: SetListComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SetListDetailComponent,
    resolve: {
      setList: SetListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SetListUpdateComponent,
    resolve: {
      setList: SetListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SetListUpdateComponent,
    resolve: {
      setList: SetListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(setListRoute)],
  exports: [RouterModule],
})
export class SetListRoutingModule {}
