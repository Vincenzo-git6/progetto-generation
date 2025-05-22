import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Action} from 'rxjs/internal/scheduler/Action';
import {Actions} from '../model/Actions';
import {GameStateService} from './game-state.service';

@Injectable({
  providedIn: 'root'
})
export class ActionRepositoryService {

  constructor(private http: HttpClient,private gsService:GameStateService) { }


  getActionByPgId()
  {
    //solo per prova

    this.gsService.gameState!.currentEntity=1
    return this.http.post<Actions[]>('api/start/action',this.gsService.gameState)
  }
}
