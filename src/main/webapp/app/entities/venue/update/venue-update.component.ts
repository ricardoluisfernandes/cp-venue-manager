import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVenue, Venue } from '../venue.model';
import { VenueService } from '../service/venue.service';
import { IContractor } from 'app/entities/contractor/contractor.model';
import { ContractorService } from 'app/entities/contractor/service/contractor.service';
import { Type } from 'app/entities/enumerations/type.model';
import { VenueStatus } from 'app/entities/enumerations/venue-status.model';

@Component({
  selector: 'jhi-venue-update',
  templateUrl: './venue-update.component.html',
})
export class VenueUpdateComponent implements OnInit {
  isSaving = false;
  typeValues = Object.keys(Type);
  venueStatusValues = Object.keys(VenueStatus);

  contractorsSharedCollection: IContractor[] = [];

  editForm = this.fb.group({
    id: [],
    instant: [null, [Validators.required]],
    location: [null, [Validators.required]],
    distance: [null, [Validators.required]],
    type: [null, [Validators.required]],
    status: [null, [Validators.required]],
    totalDuration: [],
    value: [null, [Validators.min(0)]],
    considerTravelExpenses: [],
    travelExpensesValue: [],
    doValueRetention: [],
    retentionPercentage: [],
    retentionValue: [],
    memberValue: [],
    comments: [],
    contractor: [],
  });

  constructor(
    protected venueService: VenueService,
    protected contractorService: ContractorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venue }) => {
      if (venue.id === undefined) {
        const today = dayjs().startOf('day');
        venue.instant = today;
      }

      this.updateForm(venue);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venue = this.createFromForm();
    if (venue.id !== undefined) {
      this.subscribeToSaveResponse(this.venueService.update(venue));
    } else {
      this.subscribeToSaveResponse(this.venueService.create(venue));
    }
  }

  trackContractorById(_index: number, item: IContractor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenue>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venue: IVenue): void {
    this.editForm.patchValue({
      id: venue.id,
      instant: venue.instant ? venue.instant.format(DATE_TIME_FORMAT) : null,
      location: venue.location,
      distance: venue.distance,
      type: venue.type,
      status: venue.status,
      totalDuration: venue.totalDuration,
      value: venue.value,
      considerTravelExpenses: venue.considerTravelExpenses,
      travelExpensesValue: venue.travelExpensesValue,
      doValueRetention: venue.doValueRetention,
      retentionPercentage: venue.retentionPercentage,
      retentionValue: venue.retentionValue,
      memberValue: venue.memberValue,
      comments: venue.comments,
      contractor: venue.contractor,
    });

    this.contractorsSharedCollection = this.contractorService.addContractorToCollectionIfMissing(
      this.contractorsSharedCollection,
      venue.contractor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractorService
      .query()
      .pipe(map((res: HttpResponse<IContractor[]>) => res.body ?? []))
      .pipe(
        map((contractors: IContractor[]) =>
          this.contractorService.addContractorToCollectionIfMissing(contractors, this.editForm.get('contractor')!.value)
        )
      )
      .subscribe((contractors: IContractor[]) => (this.contractorsSharedCollection = contractors));
  }

  protected createFromForm(): IVenue {
    return {
      ...new Venue(),
      id: this.editForm.get(['id'])!.value,
      instant: this.editForm.get(['instant'])!.value ? dayjs(this.editForm.get(['instant'])!.value, DATE_TIME_FORMAT) : undefined,
      location: this.editForm.get(['location'])!.value,
      distance: this.editForm.get(['distance'])!.value,
      type: this.editForm.get(['type'])!.value,
      status: this.editForm.get(['status'])!.value,
      totalDuration: this.editForm.get(['totalDuration'])!.value,
      value: this.editForm.get(['value'])!.value,
      considerTravelExpenses: this.editForm.get(['considerTravelExpenses'])!.value,
      travelExpensesValue: this.editForm.get(['travelExpensesValue'])!.value,
      doValueRetention: this.editForm.get(['doValueRetention'])!.value,
      retentionPercentage: this.editForm.get(['retentionPercentage'])!.value,
      retentionValue: this.editForm.get(['retentionValue'])!.value,
      memberValue: this.editForm.get(['memberValue'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      contractor: this.editForm.get(['contractor'])!.value,
    };
  }
}
