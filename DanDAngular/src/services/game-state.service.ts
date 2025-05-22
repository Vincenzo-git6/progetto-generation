import { Injectable } from '@angular/core';
import {GameState} from '../model/GameState';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameStateService {

  gameState : GameState | null = null;

  constructor(private http: HttpClient)
  {
      this.iniziaGame() //questo sarà da chiamare quando inizia il gioco, lo abbiamo messo solo per prova adesso
  }

  iniziaGame()
  {
      //da far scegliere al giocatore
      let listaId= [1,2,3]; //dal bottone inizia game dobbiamo passare al selector pg e far scegliere agli utenti 3 pg
                                      // i cui id dovranno riempire la lista
                                      // suggerimento: per ora mettete un pulsante per far eseguire la mossa al mostro, non sarà eseguita in automatico,
 //                                      questo pulsante farà fare una mossa casuale al mostro
      this.http.post<GameState>("/api/start", listaId).subscribe(resp=>this.gameState=resp);
  }

}
