import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SetListDetailComponent } from './set-list-detail.component';

describe('SetList Management Detail Component', () => {
  let comp: SetListDetailComponent;
  let fixture: ComponentFixture<SetListDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SetListDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ setList: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SetListDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SetListDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load setList on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.setList).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
