import { Component } from '@angular/core';
import {Actions} from '../../../model/Actions';
import {ActionRepositoryService} from '../../../services/action-repository';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-action-panel',
  imports: [
    NgForOf
  ],
  templateUrl: './action-panel.component.html',
  styleUrl: './action-panel.component.css'
})
export class ActionPanelComponent
{
  actions:Actions[]=[];
  constructor(private aRepo:ActionRepositoryService)
  {
    setTimeout(()=>{
      aRepo.getActionByPgId().subscribe(action => this.actions = action);
    },1000)

  }
}
