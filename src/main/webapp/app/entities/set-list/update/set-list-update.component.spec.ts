import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SetListService } from '../service/set-list.service';
import { ISetList, SetList } from '../set-list.model';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';

import { SetListUpdateComponent } from './set-list-update.component';

describe('SetList Management Update Component', () => {
  let comp: SetListUpdateComponent;
  let fixture: ComponentFixture<SetListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let setListService: SetListService;
  let venueService: VenueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SetListUpdateComponent],
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
      .overrideTemplate(SetListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SetListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    setListService = TestBed.inject(SetListService);
    venueService = TestBed.inject(VenueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call venue query and add missing value', () => {
      const setList: ISetList = { id: 456 };
      const venue: IVenue = { id: 42778 };
      setList.venue = venue;

      const venueCollection: IVenue[] = [{ id: 37698 }];
      jest.spyOn(venueService, 'query').mockReturnValue(of(new HttpResponse({ body: venueCollection })));
      const expectedCollection: IVenue[] = [venue, ...venueCollection];
      jest.spyOn(venueService, 'addVenueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ setList });
      comp.ngOnInit();

      expect(venueService.query).toHaveBeenCalled();
      expect(venueService.addVenueToCollectionIfMissing).toHaveBeenCalledWith(venueCollection, venue);
      expect(comp.venuesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const setList: ISetList = { id: 456 };
      const venue: IVenue = { id: 51180 };
      setList.venue = venue;

      activatedRoute.data = of({ setList });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(setList));
      expect(comp.venuesCollection).toContain(venue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SetList>>();
      const setList = { id: 123 };
      jest.spyOn(setListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setList }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(setListService.update).toHaveBeenCalledWith(setList);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SetList>>();
      const setList = new SetList();
      jest.spyOn(setListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setList }));
      saveSubject.complete();

      // THEN
      expect(setListService.create).toHaveBeenCalledWith(setList);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SetList>>();
      const setList = { id: 123 };
      jest.spyOn(setListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(setListService.update).toHaveBeenCalledWith(setList);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVenueById', () => {
      it('Should return tracked Venue primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVenueById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
