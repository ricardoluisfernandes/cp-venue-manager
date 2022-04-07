import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISetListLine } from '../set-list-line.model';

@Component({
  selector: 'jhi-set-list-line-detail',
  templateUrl: './set-list-line-detail.component.html',
})
export class SetListLineDetailComponent implements OnInit {
  setListLine: ISetListLine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setListLine }) => {
      this.setListLine = setListLine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
