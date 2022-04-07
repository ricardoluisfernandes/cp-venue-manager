import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContractor, Contractor } from '../contractor.model';
import { ContractorService } from '../service/contractor.service';

@Component({
  selector: 'jhi-contractor-update',
  templateUrl: './contractor-update.component.html',
})
export class ContractorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    email: [],
    phoneNumber: [],
    comments: [],
  });

  constructor(protected contractorService: ContractorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractor }) => {
      this.updateForm(contractor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractor = this.createFromForm();
    if (contractor.id !== undefined) {
      this.subscribeToSaveResponse(this.contractorService.update(contractor));
    } else {
      this.subscribeToSaveResponse(this.contractorService.create(contractor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractor>>): void {
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

  protected updateForm(contractor: IContractor): void {
    this.editForm.patchValue({
      id: contractor.id,
      name: contractor.name,
      email: contractor.email,
      phoneNumber: contractor.phoneNumber,
      comments: contractor.comments,
    });
  }

  protected createFromForm(): IContractor {
    return {
      ...new Contractor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      comments: this.editForm.get(['comments'])!.value,
    };
  }
}
