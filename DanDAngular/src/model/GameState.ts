import {Pg} from "./Pg";
import {Monster} from './Monster';


export interface GameState
{
  pgs : Pg[];
  monsters : Monster[];
  order : number[];
  currentEntity : number;
}
