import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MemberAvailabilityService } from '../service/member-availability.service';
import { IMemberAvailability, MemberAvailability } from '../member-availability.model';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';

import { MemberAvailabilityUpdateComponent } from './member-availability-update.component';

describe('MemberAvailability Management Update Component', () => {
  let comp: MemberAvailabilityUpdateComponent;
  let fixture: ComponentFixture<MemberAvailabilityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let memberAvailabilityService: MemberAvailabilityService;
  let memberService: MemberService;
  let venueService: VenueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MemberAvailabilityUpdateComponent],
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
      .overrideTemplate(MemberAvailabilityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MemberAvailabilityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    memberAvailabilityService = TestBed.inject(MemberAvailabilityService);
    memberService = TestBed.inject(MemberService);
    venueService = TestBed.inject(VenueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Member query and add missing value', () => {
      const memberAvailability: IMemberAvailability = { id: 456 };
      const member: IMember = { id: 33888 };
      memberAvailability.member = member;

      const memberCollection: IMember[] = [{ id: 43295 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ memberAvailability });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(memberCollection, ...additionalMembers);
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Venue query and add missing value', () => {
      const memberAvailability: IMemberAvailability = { id: 456 };
      const venue: IVenue = { id: 48213 };
      memberAvailability.venue = venue;

      const venueCollection: IVenue[] = [{ id: 77919 }];
      jest.spyOn(venueService, 'query').mockReturnValue(of(new HttpResponse({ body: venueCollection })));
      const additionalVenues = [venue];
      const expectedCollection: IVenue[] = [...additionalVenues, ...venueCollection];
      jest.spyOn(venueService, 'addVenueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ memberAvailability });
      comp.ngOnInit();

      expect(venueService.query).toHaveBeenCalled();
      expect(venueService.addVenueToCollectionIfMissing).toHaveBeenCalledWith(venueCollection, ...additionalVenues);
      expect(comp.venuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const memberAvailability: IMemberAvailability = { id: 456 };
      const member: IMember = { id: 16013 };
      memberAvailability.member = member;
      const venue: IVenue = { id: 96078 };
      memberAvailability.venue = venue;

      activatedRoute.data = of({ memberAvailability });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(memberAvailability));
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.venuesSharedCollection).toContain(venue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MemberAvailability>>();
      const memberAvailability = { id: 123 };
      jest.spyOn(memberAvailabilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberAvailability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: memberAvailability }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(memberAvailabilityService.update).toHaveBeenCalledWith(memberAvailability);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MemberAvailability>>();
      const memberAvailability = new MemberAvailability();
      jest.spyOn(memberAvailabilityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberAvailability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: memberAvailability }));
      saveSubject.complete();

      // THEN
      expect(memberAvailabilityService.create).toHaveBeenCalledWith(memberAvailability);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MemberAvailability>>();
      const memberAvailability = { id: 123 };
      jest.spyOn(memberAvailabilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberAvailability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(memberAvailabilityService.update).toHaveBeenCalledWith(memberAvailability);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMemberById', () => {
      it('Should return tracked Member primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMemberById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackVenueById', () => {
      it('Should return tracked Venue primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVenueById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
