import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMemberAvailability, MemberAvailability } from '../member-availability.model';
import { MemberAvailabilityService } from '../service/member-availability.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';
import { Availability } from 'app/entities/enumerations/availability.model';

@Component({
  selector: 'jhi-member-availability-update',
  templateUrl: './member-availability-update.component.html',
})
export class MemberAvailabilityUpdateComponent implements OnInit {
  isSaving = false;
  availabilityValues = Object.keys(Availability);

  membersSharedCollection: IMember[] = [];
  venuesSharedCollection: IVenue[] = [];

  editForm = this.fb.group({
    id: [],
    availability: [null, [Validators.required]],
    member: [],
    venue: [],
  });

  constructor(
    protected memberAvailabilityService: MemberAvailabilityService,
    protected memberService: MemberService,
    protected venueService: VenueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ memberAvailability }) => {
      this.updateForm(memberAvailability);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const memberAvailability = this.createFromForm();
    if (memberAvailability.id !== undefined) {
      this.subscribeToSaveResponse(this.memberAvailabilityService.update(memberAvailability));
    } else {
      this.subscribeToSaveResponse(this.memberAvailabilityService.create(memberAvailability));
    }
  }

  trackMemberById(_index: number, item: IMember): number {
    return item.id!;
  }

  trackVenueById(_index: number, item: IVenue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberAvailability>>): void {
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

  protected updateForm(memberAvailability: IMemberAvailability): void {
    this.editForm.patchValue({
      id: memberAvailability.id,
      availability: memberAvailability.availability,
      member: memberAvailability.member,
      venue: memberAvailability.venue,
    });

    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing(
      this.membersSharedCollection,
      memberAvailability.member
    );
    this.venuesSharedCollection = this.venueService.addVenueToCollectionIfMissing(this.venuesSharedCollection, memberAvailability.venue);
  }

  protected loadRelationshipsOptions(): void {
    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing(members, this.editForm.get('member')!.value)))
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.venueService
      .query()
      .pipe(map((res: HttpResponse<IVenue[]>) => res.body ?? []))
      .pipe(map((venues: IVenue[]) => this.venueService.addVenueToCollectionIfMissing(venues, this.editForm.get('venue')!.value)))
      .subscribe((venues: IVenue[]) => (this.venuesSharedCollection = venues));
  }

  protected createFromForm(): IMemberAvailability {
    return {
      ...new MemberAvailability(),
      id: this.editForm.get(['id'])!.value,
      availability: this.editForm.get(['availability'])!.value,
      member: this.editForm.get(['member'])!.value,
      venue: this.editForm.get(['venue'])!.value,
    };
  }
}
