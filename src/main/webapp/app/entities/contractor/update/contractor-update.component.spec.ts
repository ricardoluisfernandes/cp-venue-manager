import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContractorService } from '../service/contractor.service';
import { IContractor, Contractor } from '../contractor.model';

import { ContractorUpdateComponent } from './contractor-update.component';

describe('Contractor Management Update Component', () => {
  let comp: ContractorUpdateComponent;
  let fixture: ComponentFixture<ContractorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractorService: ContractorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContractorUpdateComponent],
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
      .overrideTemplate(ContractorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractorService = TestBed.inject(ContractorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contractor: IContractor = { id: 456 };

      activatedRoute.data = of({ contractor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contractor));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contractor>>();
      const contractor = { id: 123 };
      jest.spyOn(contractorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractorService.update).toHaveBeenCalledWith(contractor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contractor>>();
      const contractor = new Contractor();
      jest.spyOn(contractorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractor }));
      saveSubject.complete();

      // THEN
      expect(contractorService.create).toHaveBeenCalledWith(contractor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contractor>>();
      const contractor = { id: 123 };
      jest.spyOn(contractorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractorService.update).toHaveBeenCalledWith(contractor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
