import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExpenseService } from '../service/expense.service';
import { IExpense, Expense } from '../expense.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';

import { ExpenseUpdateComponent } from './expense-update.component';

describe('Expense Management Update Component', () => {
  let comp: ExpenseUpdateComponent;
  let fixture: ComponentFixture<ExpenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseService: ExpenseService;
  let paymentService: PaymentService;
  let memberService: MemberService;
  let venueService: VenueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExpenseUpdateComponent],
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
      .overrideTemplate(ExpenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseService = TestBed.inject(ExpenseService);
    paymentService = TestBed.inject(PaymentService);
    memberService = TestBed.inject(MemberService);
    venueService = TestBed.inject(VenueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call payment query and add missing value', () => {
      const expense: IExpense = { id: 456 };
      const payment: IPayment = { id: 82552 };
      expense.payment = payment;

      const paymentCollection: IPayment[] = [{ id: 46528 }];
      jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const expectedCollection: IPayment[] = [payment, ...paymentCollection];
      jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, payment);
      expect(comp.paymentsCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const expense: IExpense = { id: 456 };
      const member: IMember = { id: 11384 };
      expense.member = member;

      const memberCollection: IMember[] = [{ id: 49526 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(memberCollection, ...additionalMembers);
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Venue query and add missing value', () => {
      const expense: IExpense = { id: 456 };
      const venue: IVenue = { id: 21664 };
      expense.venue = venue;

      const venueCollection: IVenue[] = [{ id: 43290 }];
      jest.spyOn(venueService, 'query').mockReturnValue(of(new HttpResponse({ body: venueCollection })));
      const additionalVenues = [venue];
      const expectedCollection: IVenue[] = [...additionalVenues, ...venueCollection];
      jest.spyOn(venueService, 'addVenueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(venueService.query).toHaveBeenCalled();
      expect(venueService.addVenueToCollectionIfMissing).toHaveBeenCalledWith(venueCollection, ...additionalVenues);
      expect(comp.venuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const expense: IExpense = { id: 456 };
      const payment: IPayment = { id: 91940 };
      expense.payment = payment;
      const member: IMember = { id: 78872 };
      expense.member = member;
      const venue: IVenue = { id: 17639 };
      expense.venue = venue;

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(expense));
      expect(comp.paymentsCollection).toContain(payment);
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.venuesSharedCollection).toContain(venue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Expense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseService.update).toHaveBeenCalledWith(expense);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Expense>>();
      const expense = new Expense();
      jest.spyOn(expenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(expenseService.create).toHaveBeenCalledWith(expense);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Expense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseService.update).toHaveBeenCalledWith(expense);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPaymentById', () => {
      it('Should return tracked Payment primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaymentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

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
