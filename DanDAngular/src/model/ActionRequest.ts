import {GameState} from './GameState';

export interface ActionRequest
{
  previousGameState : GameState[];
  target : number[];
  actionType : string;
}
