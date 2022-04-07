import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISetList } from '../set-list.model';

@Component({
  selector: 'jhi-set-list-detail',
  templateUrl: './set-list-detail.component.html',
})
export class SetListDetailComponent implements OnInit {
  setList: ISetList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setList }) => {
      this.setList = setList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
