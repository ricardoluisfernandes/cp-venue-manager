import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISetList, SetList } from '../set-list.model';
import { SetListService } from '../service/set-list.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';

@Component({
  selector: 'jhi-set-list-update',
  templateUrl: './set-list-update.component.html',
})
export class SetListUpdateComponent implements OnInit {
  isSaving = false;

  venuesCollection: IVenue[] = [];

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    totalDuration: [],
    venue: [],
  });

  constructor(
    protected setListService: SetListService,
    protected venueService: VenueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setList }) => {
      this.updateForm(setList);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const setList = this.createFromForm();
    if (setList.id !== undefined) {
      this.subscribeToSaveResponse(this.setListService.update(setList));
    } else {
      this.subscribeToSaveResponse(this.setListService.create(setList));
    }
  }

  trackVenueById(_index: number, item: IVenue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISetList>>): void {
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

  protected updateForm(setList: ISetList): void {
    this.editForm.patchValue({
      id: setList.id,
      description: setList.description,
      totalDuration: setList.totalDuration,
      venue: setList.venue,
    });

    this.venuesCollection = this.venueService.addVenueToCollectionIfMissing(this.venuesCollection, setList.venue);
  }

  protected loadRelationshipsOptions(): void {
    this.venueService
      .query({ filter: 'setlist-is-null' })
      .pipe(map((res: HttpResponse<IVenue[]>) => res.body ?? []))
      .pipe(map((venues: IVenue[]) => this.venueService.addVenueToCollectionIfMissing(venues, this.editForm.get('venue')!.value)))
      .subscribe((venues: IVenue[]) => (this.venuesCollection = venues));
  }

  protected createFromForm(): ISetList {
    return {
      ...new SetList(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      totalDuration: this.editForm.get(['totalDuration'])!.value,
      venue: this.editForm.get(['venue'])!.value,
    };
  }
}
