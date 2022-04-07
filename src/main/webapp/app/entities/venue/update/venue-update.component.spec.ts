import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VenueService } from '../service/venue.service';
import { IVenue, Venue } from '../venue.model';
import { IContractor } from 'app/entities/contractor/contractor.model';
import { ContractorService } from 'app/entities/contractor/service/contractor.service';

import { VenueUpdateComponent } from './venue-update.component';

describe('Venue Management Update Component', () => {
  let comp: VenueUpdateComponent;
  let fixture: ComponentFixture<VenueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let venueService: VenueService;
  let contractorService: ContractorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VenueUpdateComponent],
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
      .overrideTemplate(VenueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VenueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    venueService = TestBed.inject(VenueService);
    contractorService = TestBed.inject(ContractorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contractor query and add missing value', () => {
      const venue: IVenue = { id: 456 };
      const contractor: IContractor = { id: 64746 };
      venue.contractor = contractor;

      const contractorCollection: IContractor[] = [{ id: 67664 }];
      jest.spyOn(contractorService, 'query').mockReturnValue(of(new HttpResponse({ body: contractorCollection })));
      const additionalContractors = [contractor];
      const expectedCollection: IContractor[] = [...additionalContractors, ...contractorCollection];
      jest.spyOn(contractorService, 'addContractorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venue });
      comp.ngOnInit();

      expect(contractorService.query).toHaveBeenCalled();
      expect(contractorService.addContractorToCollectionIfMissing).toHaveBeenCalledWith(contractorCollection, ...additionalContractors);
      expect(comp.contractorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const venue: IVenue = { id: 456 };
      const contractor: IContractor = { id: 5150 };
      venue.contractor = contractor;

      activatedRoute.data = of({ venue });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(venue));
      expect(comp.contractorsSharedCollection).toContain(contractor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Venue>>();
      const venue = { id: 123 };
      jest.spyOn(venueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venue }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(venueService.update).toHaveBeenCalledWith(venue);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Venue>>();
      const venue = new Venue();
      jest.spyOn(venueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venue }));
      saveSubject.complete();

      // THEN
      expect(venueService.create).toHaveBeenCalledWith(venue);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Venue>>();
      const venue = { id: 123 };
      jest.spyOn(venueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(venueService.update).toHaveBeenCalledWith(venue);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContractorById', () => {
      it('Should return tracked Contractor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContractorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
