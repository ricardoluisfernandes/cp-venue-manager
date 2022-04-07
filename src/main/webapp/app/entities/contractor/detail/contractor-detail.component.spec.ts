import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContractorDetailComponent } from './contractor-detail.component';

describe('Contractor Management Detail Component', () => {
  let comp: ContractorDetailComponent;
  let fixture: ComponentFixture<ContractorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContractorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contractor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContractorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContractorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contractor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contractor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
