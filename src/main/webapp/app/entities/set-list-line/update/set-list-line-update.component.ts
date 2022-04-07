import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISetListLine, SetListLine } from '../set-list-line.model';
import { SetListLineService } from '../service/set-list-line.service';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { ISetList } from 'app/entities/set-list/set-list.model';
import { SetListService } from 'app/entities/set-list/service/set-list.service';

@Component({
  selector: 'jhi-set-list-line-update',
  templateUrl: './set-list-line-update.component.html',
})
export class SetListLineUpdateComponent implements OnInit {
  isSaving = false;

  songsSharedCollection: ISong[] = [];
  setListsSharedCollection: ISetList[] = [];

  editForm = this.fb.group({
    id: [],
    order: [null, [Validators.required]],
    song: [],
    setList: [],
  });

  constructor(
    protected setListLineService: SetListLineService,
    protected songService: SongService,
    protected setListService: SetListService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setListLine }) => {
      this.updateForm(setListLine);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const setListLine = this.createFromForm();
    if (setListLine.id !== undefined) {
      this.subscribeToSaveResponse(this.setListLineService.update(setListLine));
    } else {
      this.subscribeToSaveResponse(this.setListLineService.create(setListLine));
    }
  }

  trackSongById(_index: number, item: ISong): number {
    return item.id!;
  }

  trackSetListById(_index: number, item: ISetList): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISetListLine>>): void {
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

  protected updateForm(setListLine: ISetListLine): void {
    this.editForm.patchValue({
      id: setListLine.id,
      order: setListLine.order,
      song: setListLine.song,
      setList: setListLine.setList,
    });

    this.songsSharedCollection = this.songService.addSongToCollectionIfMissing(this.songsSharedCollection, setListLine.song);
    this.setListsSharedCollection = this.setListService.addSetListToCollectionIfMissing(this.setListsSharedCollection, setListLine.setList);
  }

  protected loadRelationshipsOptions(): void {
    this.songService
      .query()
      .pipe(map((res: HttpResponse<ISong[]>) => res.body ?? []))
      .pipe(map((songs: ISong[]) => this.songService.addSongToCollectionIfMissing(songs, this.editForm.get('song')!.value)))
      .subscribe((songs: ISong[]) => (this.songsSharedCollection = songs));

    this.setListService
      .query()
      .pipe(map((res: HttpResponse<ISetList[]>) => res.body ?? []))
      .pipe(
        map((setLists: ISetList[]) => this.setListService.addSetListToCollectionIfMissing(setLists, this.editForm.get('setList')!.value))
      )
      .subscribe((setLists: ISetList[]) => (this.setListsSharedCollection = setLists));
  }

  protected createFromForm(): ISetListLine {
    return {
      ...new SetListLine(),
      id: this.editForm.get(['id'])!.value,
      order: this.editForm.get(['order'])!.value,
      song: this.editForm.get(['song'])!.value,
      setList: this.editForm.get(['setList'])!.value,
    };
  }
}
