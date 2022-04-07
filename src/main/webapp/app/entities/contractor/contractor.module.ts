import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContractorComponent } from './list/contractor.component';
import { ContractorDetailComponent } from './detail/contractor-detail.component';
import { ContractorUpdateComponent } from './update/contractor-update.component';
import { ContractorDeleteDialogComponent } from './delete/contractor-delete-dialog.component';
import { ContractorRoutingModule } from './route/contractor-routing.module';

@NgModule({
  imports: [SharedModule, ContractorRoutingModule],
  declarations: [ContractorComponent, ContractorDetailComponent, ContractorUpdateComponent, ContractorDeleteDialogComponent],
  entryComponents: [ContractorDeleteDialogComponent],
})
export class ContractorModule {}
