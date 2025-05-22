import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GameStateDto, ActionRequest, PgDto, MonsterDto } from '../model/frontend-models';

@Injectable({
  providedIn: 'root'
})
export class CombatClientService {
  private apiUrl = '/api/start';

  constructor(private http: HttpClient) { }

  startNewGame(pgIds: number[]): Observable<GameStateDto> {
    return this.http.post<GameStateDto>(this.apiUrl, pgIds)
      .pipe(catchError(this.handleError));
  }

  performAction(request: ActionRequest): Observable<GameStateDto> {
    return this.http.post<GameStateDto>(`${this.apiUrl}/actionEntity`, request)
      .pipe(catchError(this.handleError));
  }

  advanceToNextEntity(currentState: GameStateDto): Observable<GameStateDto> {
    return this.http.post<GameStateDto>(`${this.apiUrl}/next`, currentState)
      .pipe(catchError(this.handleError));
  }

  checkBattleOver(currentState: GameStateDto): Observable<string> {
    return this.http.post(`${this.apiUrl}/battleOver`, currentState, { responseType: 'text' })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Si è verificato un errore sconosciuto!';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Errore: ${error.error.message}`;
    } else {
      errorMessage = `Codice Errore Server: ${error.status}\nMessaggio: ${error.message}`;
      if (error.error && typeof error.error === 'string') {
        errorMessage += `\nDettagli: ${error.error}`;
      }
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  getActiveCharacterFromState(gameState: GameStateDto): PgDto | MonsterDto | undefined {
    if (!gameState || (!gameState.good && !gameState.evil)) {
      return undefined;
    }
    const activePg = gameState.good?.find(pg => pg.id === gameState.currentEntity);
    if (activePg) {
      return activePg;
    }
    return gameState.evil?.find(monster => monster.id === gameState.currentEntity);
  }

  isPlayerCharacter(characterId: number, gameState: GameStateDto): boolean {
    return !!gameState.good?.find(pg => pg.id === characterId);
  }
}
