import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMember, Member } from '../member.model';
import { MemberService } from '../service/member.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';
import { VoiceType } from 'app/entities/enumerations/voice-type.model';

@Component({
  selector: 'jhi-member-update',
  templateUrl: './member-update.component.html',
})
export class MemberUpdateComponent implements OnInit {
  isSaving = false;
  voiceTypeValues = Object.keys(VoiceType);

  venuesSharedCollection: IVenue[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    email: [null, [Validators.required]],
    phoneNumber: [],
    voiceType: [null, [Validators.required]],
    venues: [],
  });

  constructor(
    protected memberService: MemberService,
    protected venueService: VenueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.updateForm(member);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const member = this.createFromForm();
    if (member.id !== undefined) {
      this.subscribeToSaveResponse(this.memberService.update(member));
    } else {
      this.subscribeToSaveResponse(this.memberService.create(member));
    }
  }

  trackVenueById(_index: number, item: IVenue): number {
    return item.id!;
  }

  getSelectedVenue(option: IVenue, selectedVals?: IVenue[]): IVenue {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>): void {
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

  protected updateForm(member: IMember): void {
    this.editForm.patchValue({
      id: member.id,
      firstName: member.firstName,
      lastName: member.lastName,
      email: member.email,
      phoneNumber: member.phoneNumber,
      voiceType: member.voiceType,
      venues: member.venues,
    });

    this.venuesSharedCollection = this.venueService.addVenueToCollectionIfMissing(this.venuesSharedCollection, ...(member.venues ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.venueService
      .query()
      .pipe(map((res: HttpResponse<IVenue[]>) => res.body ?? []))
      .pipe(
        map((venues: IVenue[]) => this.venueService.addVenueToCollectionIfMissing(venues, ...(this.editForm.get('venues')!.value ?? [])))
      )
      .subscribe((venues: IVenue[]) => (this.venuesSharedCollection = venues));
  }

  protected createFromForm(): IMember {
    return {
      ...new Member(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      voiceType: this.editForm.get(['voiceType'])!.value,
      venues: this.editForm.get(['venues'])!.value,
    };
  }
}
