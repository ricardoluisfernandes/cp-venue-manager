import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContractorComponent } from '../list/contractor.component';
import { ContractorDetailComponent } from '../detail/contractor-detail.component';
import { ContractorUpdateComponent } from '../update/contractor-update.component';
import { ContractorRoutingResolveService } from './contractor-routing-resolve.service';

const contractorRoute: Routes = [
  {
    path: '',
    component: ContractorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractorDetailComponent,
    resolve: {
      contractor: ContractorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractorUpdateComponent,
    resolve: {
      contractor: ContractorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractorUpdateComponent,
    resolve: {
      contractor: ContractorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contractorRoute)],
  exports: [RouterModule],
})
export class ContractorRoutingModule {}
