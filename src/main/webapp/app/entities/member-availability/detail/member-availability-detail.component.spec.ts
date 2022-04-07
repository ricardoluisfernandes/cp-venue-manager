import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MemberAvailabilityDetailComponent } from './member-availability-detail.component';

describe('MemberAvailability Management Detail Component', () => {
  let comp: MemberAvailabilityDetailComponent;
  let fixture: ComponentFixture<MemberAvailabilityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemberAvailabilityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ memberAvailability: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MemberAvailabilityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MemberAvailabilityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load memberAvailability on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.memberAvailability).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
