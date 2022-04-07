import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISong, Song } from '../song.model';
import { SongService } from '../service/song.service';

@Component({
  selector: 'jhi-song-update',
  templateUrl: './song-update.component.html',
})
export class SongUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    duration: [null, [Validators.required]],
    score: [],
  });

  constructor(protected songService: SongService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ song }) => {
      this.updateForm(song);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const song = this.createFromForm();
    if (song.id !== undefined) {
      this.subscribeToSaveResponse(this.songService.update(song));
    } else {
      this.subscribeToSaveResponse(this.songService.create(song));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISong>>): void {
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

  protected updateForm(song: ISong): void {
    this.editForm.patchValue({
      id: song.id,
      name: song.name,
      duration: song.duration,
      score: song.score,
    });
  }

  protected createFromForm(): ISong {
    return {
      ...new Song(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      score: this.editForm.get(['score'])!.value,
    };
  }
}
