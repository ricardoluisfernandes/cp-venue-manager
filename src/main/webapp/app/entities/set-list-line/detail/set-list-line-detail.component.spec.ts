import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SetListLineDetailComponent } from './set-list-line-detail.component';

describe('SetListLine Management Detail Component', () => {
  let comp: SetListLineDetailComponent;
  let fixture: ComponentFixture<SetListLineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SetListLineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ setListLine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SetListLineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SetListLineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load setListLine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.setListLine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
