import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaymentService } from '../service/payment.service';
import { IPayment, Payment } from '../payment.model';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';

import { PaymentUpdateComponent } from './payment-update.component';

describe('Payment Management Update Component', () => {
  let comp: PaymentUpdateComponent;
  let fixture: ComponentFixture<PaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentService: PaymentService;
  let memberService: MemberService;
  let venueService: VenueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaymentUpdateComponent],
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
      .overrideTemplate(PaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentService = TestBed.inject(PaymentService);
    memberService = TestBed.inject(MemberService);
    venueService = TestBed.inject(VenueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Member query and add missing value', () => {
      const payment: IPayment = { id: 456 };
      const member: IMember = { id: 34709 };
      payment.member = member;

      const memberCollection: IMember[] = [{ id: 73222 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(memberCollection, ...additionalMembers);
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Venue query and add missing value', () => {
      const payment: IPayment = { id: 456 };
      const venue: IVenue = { id: 77052 };
      payment.venue = venue;

      const venueCollection: IVenue[] = [{ id: 90677 }];
      jest.spyOn(venueService, 'query').mockReturnValue(of(new HttpResponse({ body: venueCollection })));
      const additionalVenues = [venue];
      const expectedCollection: IVenue[] = [...additionalVenues, ...venueCollection];
      jest.spyOn(venueService, 'addVenueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(venueService.query).toHaveBeenCalled();
      expect(venueService.addVenueToCollectionIfMissing).toHaveBeenCalledWith(venueCollection, ...additionalVenues);
      expect(comp.venuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const payment: IPayment = { id: 456 };
      const member: IMember = { id: 84643 };
      payment.member = member;
      const venue: IVenue = { id: 70946 };
      payment.venue = venue;

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(payment));
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.venuesSharedCollection).toContain(venue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Payment>>();
      const payment = { id: 123 };
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentService.update).toHaveBeenCalledWith(payment);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Payment>>();
      const payment = new Payment();
      jest.spyOn(paymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(paymentService.create).toHaveBeenCalledWith(payment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Payment>>();
      const payment = { id: 123 };
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentService.update).toHaveBeenCalledWith(payment);
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
