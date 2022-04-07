import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SetListLineService } from '../service/set-list-line.service';
import { ISetListLine, SetListLine } from '../set-list-line.model';
import { ISong } from 'app/entities/song/song.model';
import { SongService } from 'app/entities/song/service/song.service';
import { ISetList } from 'app/entities/set-list/set-list.model';
import { SetListService } from 'app/entities/set-list/service/set-list.service';

import { SetListLineUpdateComponent } from './set-list-line-update.component';

describe('SetListLine Management Update Component', () => {
  let comp: SetListLineUpdateComponent;
  let fixture: ComponentFixture<SetListLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let setListLineService: SetListLineService;
  let songService: SongService;
  let setListService: SetListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SetListLineUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SetListLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SetListLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    setListLineService = TestBed.inject(SetListLineService);
    songService = TestBed.inject(SongService);
    setListService = TestBed.inject(SetListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Song query and add missing value', () => {
      const setListLine: ISetListLine = { id: 456 };
      const song: ISong = { id: 75996 };
      setListLine.song = song;

      const songCollection: ISong[] = [{ id: 72818 }];
      jest.spyOn(songService, 'query').mockReturnValue(of(new HttpResponse({ body: songCollection })));
      const additionalSongs = [song];
      const expectedCollection: ISong[] = [...additionalSongs, ...songCollection];
      jest.spyOn(songService, 'addSongToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ setListLine });
      comp.ngOnInit();

      expect(songService.query).toHaveBeenCalled();
      expect(songService.addSongToCollectionIfMissing).toHaveBeenCalledWith(songCollection, ...additionalSongs);
      expect(comp.songsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SetList query and add missing value', () => {
      const setListLine: ISetListLine = { id: 456 };
      const setList: ISetList = { id: 99201 };
      setListLine.setList = setList;

      const setListCollection: ISetList[] = [{ id: 2709 }];
      jest.spyOn(setListService, 'query').mockReturnValue(of(new HttpResponse({ body: setListCollection })));
      const additionalSetLists = [setList];
      const expectedCollection: ISetList[] = [...additionalSetLists, ...setListCollection];
      jest.spyOn(setListService, 'addSetListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ setListLine });
      comp.ngOnInit();

      expect(setListService.query).toHaveBeenCalled();
      expect(setListService.addSetListToCollectionIfMissing).toHaveBeenCalledWith(setListCollection, ...additionalSetLists);
      expect(comp.setListsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const setListLine: ISetListLine = { id: 456 };
      const song: ISong = { id: 81527 };
      setListLine.song = song;
      const setList: ISetList = { id: 40840 };
      setListLine.setList = setList;

      activatedRoute.data = of({ setListLine });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(setListLine));
      expect(comp.songsSharedCollection).toContain(song);
      expect(comp.setListsSharedCollection).toContain(setList);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SetListLine>>();
      const setListLine = { id: 123 };
      jest.spyOn(setListLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setListLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setListLine }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(setListLineService.update).toHaveBeenCalledWith(setListLine);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SetListLine>>();
      const setListLine = new SetListLine();
      jest.spyOn(setListLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setListLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setListLine }));
      saveSubject.complete();

      // THEN
      expect(setListLineService.create).toHaveBeenCalledWith(setListLine);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SetListLine>>();
      const setListLine = { id: 123 };
      jest.spyOn(setListLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setListLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(setListLineService.update).toHaveBeenCalledWith(setListLine);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSongById', () => {
      it('Should return tracked Song primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSongById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSetListById', () => {
      it('Should return tracked SetList primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSetListById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
