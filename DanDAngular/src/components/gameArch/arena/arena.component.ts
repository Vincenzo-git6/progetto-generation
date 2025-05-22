import { Component, OnInit, OnDestroy, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { CombatClientService } from '../../../services/combat-client.service';
import { GameStateDto, PgDto, MonsterDto, ActionRequest, ActionType, ActionDetailDto } from '../../../model/frontend-models';

@Component({
  selector: 'app-arena',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './arena.component.html',
  styleUrls: ['./arena.component.css']
})
export class ArenaComponent implements OnInit, OnDestroy {
  @ViewChild('logEntriesContainer') private logEntriesContainer!: ElementRef;

  public gameState: GameStateDto | null = null;
  public selectedPgIds: number[] = [];
  public isLoading: boolean = true;
  public battleLog: string[] = [];
  public battleOutcome: string | null = null;
  public isPaused: boolean = false;

  public isTargetingPlayerAction: boolean = false;
  public selectedActionForTargeting: ActionDetailDto | null = null;
  public selectedTargets: (PgDto | MonsterDto)[] = [];
  public currentActingPg: PgDto | null = null;

  private routeSubscription: Subscription | undefined;
  private static readonly PG_IDS_KEY = 'selectedPgIds';

  constructor(
    private combatService: CombatClientService,
    public router: Router,
    private activatedRoute: ActivatedRoute,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const storedPgIds = sessionStorage.getItem(ArenaComponent.PG_IDS_KEY);
    if (storedPgIds) {
      this.selectedPgIds = JSON.parse(storedPgIds);
      if (this.selectedPgIds && this.selectedPgIds.length > 0) {
        this.initializeBattle();
      } else {
        this.addLogEntry('Errore: ID dei personaggi non trovati. Torno alla selezione.');
        this.isLoading = false;
        this.navigateToSelection();
      }
    } else {
      this.routeSubscription = this.activatedRoute.queryParams.subscribe(params => {
        const pgIdsParam = params['pgIds'];
        if (pgIdsParam) {
          this.selectedPgIds = pgIdsParam.split(',').map((id: string) => parseInt(id, 10)).filter((id: number) => !isNaN(id));
          sessionStorage.setItem(ArenaComponent.PG_IDS_KEY, JSON.stringify(this.selectedPgIds));
          if (this.selectedPgIds && this.selectedPgIds.length > 0) {
            this.initializeBattle();
          } else {
            this.addLogEntry('Errore: ID dei personaggi non validi dai parametri URL.');
            this.isLoading = false;
            this.navigateToSelection();
          }
        } else {
          this.addLogEntry('Errore: ID dei personaggi non forniti. Navigare da /selezione.');
          this.isLoading = false;
          this.navigateToSelection();
        }
      });
    }
  }

  // *** INIZIO NUOVA AGGIUNTA ***
  public get currentTurnCharacterAsPg(): PgDto | null {
    if (this.gameState && this.gameState.currentEntity) {
      const char = this.getCharacterById(this.gameState.currentEntity);
      // Controlla che sia un PgDto (verificando la presenza di detailedActions o un campo specifico dei PG)
      if (char && 'detailedActions' in char && this.getEntityType(char.id) === 'pg') {
        return char as PgDto;
      }
    }
    return null;
  }
  // *** FINE NUOVA AGGIUNTA ***

  private initializeBattle(): void {
    this.isLoading = true;
    this.isPaused = false;
    this.battleLog = [];
    this.addLogEntry('Inizializzazione battaglia...');
    this.battleOutcome = null;
    this.resetActionState();
    this.combatService.startNewGame(this.selectedPgIds).subscribe({
      next: (initialGameState) => {
        this.gameState = this.processIncomingGameState(initialGameState);
        this.addLogEntry('Battaglia iniziata!');
        this.isLoading = false;
        this.cdRef.detectChanges();
        this.checkIfMonsterTurn();
      },
      error: (err) => {
        this.addLogEntry(`Errore durante l'inizializzazione della battaglia: ${err.message}`);
        console.error(err);
        this.isLoading = false;
        this.cdRef.detectChanges();
      }
    });
  }

  private processIncomingGameState(newState: GameStateDto, existingState?: GameStateDto | null): GameStateDto {
    newState.good.forEach(pg => {
      const existingPg = existingState?.good.find(epg => epg.id === pg.id);
      pg.maxHp = existingPg?.maxHp || pg.hp;
      pg.currentHp = pg.hp;

      if (!pg.detailedActions) {
        console.warn(`[PROCESS_GAME_STATE] PgDto per ${pg.name} (ID: ${pg.id}) non ha 'detailedActions'.`);
        pg.detailedActions = [];
      } else {
        pg.detailedActions.forEach(action => {
          const enumValue = Object.values(ActionType).find(val => val === (action.actionType as string));
          if (enumValue) {
            action.actionType = enumValue;
          } else {
            console.warn(`[PROCESS_GAME_STATE] ActionType non valido ('${action.actionType}') per l'azione '${action.name}' del PG ${pg.name}. Default a BASE.`);
            action.actionType = ActionType.BASE;
          }
          action.targetAllies = !!action.targetAllies;
          action.targetSelf = !!action.targetSelf;
        });
      }
    });
    newState.evil.forEach(monster => {
      const existingMonster = existingState?.evil.find(em => em.id === monster.id);
      monster.maxHp = existingMonster?.maxHp || monster.hp;
      monster.currentHp = monster.hp;
    });
    return newState;
  }

  private addLogEntry(message: string): void {
    this.battleLog.push(message);
    this.scrollToBottomLog();
  }

  private scrollToBottomLog(): void {
    try {
      setTimeout(() => {
        if (this.logEntriesContainer && this.logEntriesContainer.nativeElement) {
          this.logEntriesContainer.nativeElement.scrollTop = this.logEntriesContainer.nativeElement.scrollHeight;
        }
      }, 0);
    } catch (err) {
      console.error('Errore durante lo scroll del log:', err);
    }
  }

  public getCharacterById(id: number): PgDto | MonsterDto | undefined {
    if (!this.gameState) return undefined;
    let character = this.gameState.good.find(pg => pg.id === id);
    if (character) return character;
    return this.gameState.evil.find(m => m.id === id);
  }

  public isPlayerTurn(): boolean {
    if (!this.gameState || !this.gameState.currentEntity) return false;
    return this.gameState.good.some(pg => pg.id === this.gameState!.currentEntity);
  }

  private checkIfMonsterTurn(): void {
    if (this.isPaused || this.battleOutcome || !this.gameState) return;
    if (!this.isPlayerTurn()) {
      const monster = this.getCharacterById(this.gameState.currentEntity);
      this.addLogEntry(`È il turno di ${monster?.name || 'Mostro Sconosciuto'}. Il mostro prepara un'azione...`);
      this.cdRef.detectChanges();
      setTimeout(() => this.handleMonsterAction(), 1200);
    }
  }

  private handleMonsterAction(): void {
    if (this.isPaused || this.battleOutcome || !this.gameState || this.isPlayerTurn()) return;
    const monsterActionRequest: ActionRequest = {
      previousDto: this.gameState,
      target: [],
      actionType: ActionType.BASE
    };
    this.isLoading = true;
    this.cdRef.detectChanges();
    this.combatService.performAction(monsterActionRequest).subscribe({
      next: (updatedGameStateFromServer) => {
        const actingCharacter = this.getCharacterById(this.gameState!.currentEntity);
        const oldPgsHp = this.gameState!.good.map(pg => ({ id: pg.id, hp: pg.currentHp }));
        this.gameState = this.processIncomingGameState(updatedGameStateFromServer, this.gameState);
        this.logHpChanges(oldPgsHp, this.gameState.good);
        this.addLogEntry(`${actingCharacter?.name || 'Mostro'} ha agito.`);
        this.isLoading = false;
        this.cdRef.detectChanges();
        this.proceedToNextStep();
      },
      error: (err) => {
        this.addLogEntry(`Errore durante l'azione del mostro: ${err.message}`);
        this.isLoading = false;
        this.cdRef.detectChanges();
      }
    });
  }

  private logHpChanges(
    oldEntitiesHp: { id: number; hp: number }[],
    newEntities: (PgDto | MonsterDto)[]
  ): void {
    newEntities.forEach(newEntity => {
      const oldEntity = oldEntitiesHp.find(o => o.id === newEntity.id);
      if (oldEntity) {
        const oldHp = oldEntity.hp;
        const currentHp = newEntity.currentHp;
        if (currentHp < oldHp) {
          this.addLogEntry(`${newEntity.name} subisce ${oldHp - currentHp} danni. HP rimanenti: ${currentHp}/${newEntity.maxHp}`);
        } else if (currentHp > oldHp) {
          this.addLogEntry(`${newEntity.name} viene curato di ${currentHp - oldHp}. HP attuali: ${currentHp}/${newEntity.maxHp}`);
        }
        if (currentHp <= 0 && oldHp > 0) {
          this.addLogEntry(`${newEntity.name} è stato sconfitto!`);
        }
      }
    });
  }

  public handlePlayerActionSelection(selectedAction: ActionDetailDto, actingPg: PgDto): void {
    if (this.isPaused || this.battleOutcome || !this.gameState || !this.isPlayerTurn() || selectedAction.currentCooldown > 0) {
      if (selectedAction.currentCooldown > 0) {
        this.addLogEntry(`Azione '${selectedAction.name}' è in cooldown per altri ${selectedAction.currentCooldown} turni.`);
      }
      return;
    }

    this.selectedActionForTargeting = selectedAction;
    this.currentActingPg = actingPg; // currentActingPg è ancora utile per quando si conferma l'azione
    this.selectedTargets = [];

    if (this.selectedActionForTargeting.targetSelf === true) {
      this.isTargetingPlayerAction = false;
      this.selectedTargets = [actingPg];
      this.addLogEntry(`${actingPg.name} usa ${this.selectedActionForTargeting.name} su sé stesso.`);
      this.confirmPlayerAction();
    } else {
      this.isTargetingPlayerAction = true;
      if (this.selectedActionForTargeting.maxTargets > 0) {
        this.addLogEntry(`Seleziona ${this.selectedActionForTargeting.maxTargets > 1 ? 'fino a ' : ''}${this.selectedActionForTargeting.maxTargets} ${this.selectedActionForTargeting.targetAllies === true ? 'alleato/i' : 'nemico/i'} per ${this.selectedActionForTargeting.name}.`);
      } else {
        this.addLogEntry(`Azione ${this.selectedActionForTargeting.name} selezionata.`);
      }
    }
    this.cdRef.detectChanges();
  }

  public selectTarget(target: MonsterDto | PgDto): void {
    if (this.isPaused || !this.isTargetingPlayerAction || this.battleOutcome || !this.selectedActionForTargeting) { // currentActingPg non è più necessario qui per la logica di base
      return;
    }

    const targetEntityType = this.getEntityType(target.id);
    const action = this.selectedActionForTargeting;
    const actingCharacterName = this.currentActingPg?.name || this.currentTurnCharacterAsPg?.name || "Giocatore";


    if (target.currentHp <= 0 && !action.name.toLowerCase().includes("resurrezione")) {
      this.addLogEntry(`${target.name} è sconfitto e non può essere bersagliato da questa azione.`);
      return;
    }

    const targetIndex = this.selectedTargets.findIndex(t => t.id === target.id);

    if (action.targetAllies === true) {
      if (targetEntityType === 'pg') {
        if (targetIndex > -1) {
          this.selectedTargets.splice(targetIndex, 1);
          this.addLogEntry(`${target.name} deselezionato.`);
        } else {
          if (action.maxTargets === 1) {
            this.selectedTargets = [target as PgDto];
            this.addLogEntry(`${actingCharacterName} bersaglia ${target.name} per ${action.name}.`);
          } else if (this.selectedTargets.length < action.maxTargets) {
            this.selectedTargets.push(target as PgDto);
            this.addLogEntry(`${actingCharacterName} aggiunge ${target.name} ai bersagli per ${action.name}.`);
          } else {
            this.addLogEntry(`Puoi selezionare al massimo ${action.maxTargets} alleato/i. Deseleziona un bersaglio per cambiarlo.`);
          }
        }
      } else {
        this.addLogEntry(`L'azione '${action.name}' può bersagliare solo personaggi alleati, non ${targetEntityType}.`);
      }
    } else { // targetsEnemies (o targetAllies è false)
      if (targetEntityType === 'monster') {
        if (targetIndex > -1) {
          this.selectedTargets.splice(targetIndex, 1);
          this.addLogEntry(`${target.name} deselezionato.`);
        } else {
          if (action.maxTargets === 1) {
            this.selectedTargets = [target as MonsterDto];
            this.addLogEntry(`${actingCharacterName} bersaglia ${target.name} per ${action.name}.`);
          } else if (this.selectedTargets.length < action.maxTargets) {
            this.selectedTargets.push(target as MonsterDto);
            this.addLogEntry(`${actingCharacterName} aggiunge ${target.name} ai bersagli per ${action.name}.`);
          } else {
            this.addLogEntry(`Puoi selezionare al massimo ${action.maxTargets} nemico/i. Deseleziona un bersaglio per cambiarlo.`);
          }
        }
      } else {
        this.addLogEntry(`L'azione '${action.name}' può bersagliare solo mostri nemici, non ${targetEntityType}.`);
      }
    }
    this.cdRef.detectChanges();
  }


  public isCharacterCardTargetable(character: PgDto | MonsterDto): boolean {
    if (!this.isTargetingPlayerAction || !this.selectedActionForTargeting || this.isPaused ) {
      return false;
    }
    // Non permettere di bersagliare personaggi sconfitti a meno che l'azione non sia una resurrezione
    if (character.currentHp <= 0 && !this.selectedActionForTargeting.name.toLowerCase().includes("resurrezione")) {
      return false;
    }

    const action = this.selectedActionForTargeting;
    const characterType = this.getEntityType(character.id);

    let canBeTargeted = false;
    if (action.targetAllies === true) { // L'azione bersaglia alleati
      canBeTargeted = characterType === 'pg';
    } else { // L'azione bersaglia nemici
      canBeTargeted = characterType === 'monster';
    }
    return canBeTargeted;
  }

  public isTargetSelected(entityId: number): boolean {
    if (!this.selectedTargets) return false;
    return this.selectedTargets.some(target => target.id === entityId);
  }

  public confirmPlayerAction(): void {
    const actingPgForAction = this.currentActingPg || this.currentTurnCharacterAsPg;
    if (this.isPaused || !this.gameState || !this.isPlayerTurn() || this.battleOutcome || !this.selectedActionForTargeting || !actingPgForAction) {
      this.cancelTargeting();
      return;
    }

    const action = this.selectedActionForTargeting;
    if (!action.targetSelf && action.maxTargets > 0 && this.selectedTargets.length === 0) {
      this.addLogEntry("Devi selezionare almeno un bersaglio per questa azione.");
      this.cdRef.detectChanges();
      return;
    }

    this.addLogEntry(`${actingPgForAction.name} usa l'azione ${action.name} (${action.actionType}) su ${this.selectedTargets.length > 0 ? this.selectedTargets.map(target => target.name).join(', ') : (action.targetSelf ? 'sé stesso' : 'tutti i bersagli validi')}`);

    const actionRequest: ActionRequest = {
      previousDto: this.gameState,
      target: this.selectedTargets.map(target => target.id),
      actionType: action.actionType,
      actionName: action.name // Assicurati che actionName sia inviato se il backend lo richiede
    };

    const oldPgsHp = this.gameState.good.map(pg => ({ id: pg.id, hp: pg.currentHp }));
    const oldMonstersHp = this.gameState.evil.map(m => ({ id: m.id, hp: m.currentHp }));

    this.isLoading = true;
    this.resetActionState(); // Resetta prima di inviare la richiesta
    this.cdRef.detectChanges();

    this.combatService.performAction(actionRequest).subscribe({
      next: (updatedGameStateFromServer) => {
        this.gameState = this.processIncomingGameState(updatedGameStateFromServer, this.gameState);
        this.logHpChanges(oldPgsHp, this.gameState.good);
        this.logHpChanges(oldMonstersHp, this.gameState.evil.filter(m => oldMonstersHp.find(om => om.id === m.id))); // Filtra per loggare solo mostri esistenti
        this.isLoading = false;
        this.cdRef.detectChanges();
        this.proceedToNextStep();
      },
      error: (err) => {
        this.addLogEntry(`Errore durante l'esecuzione dell'azione: ${err.message}`);
        this.isLoading = false;
        // Non resettare lo stato qui, potrebbe essere utile per debug o ritentativi
        this.cdRef.detectChanges();
      }
    });
  }


  public cancelTargeting(): void {
    this.resetActionState();
    this.addLogEntry("Selezione bersaglio annullata.");
    this.cdRef.detectChanges();
  }

  private resetActionState(): void {
    this.isTargetingPlayerAction = false;
    this.selectedActionForTargeting = null;
    this.selectedTargets = [];
    // Non resettare currentActingPg qui se lo usi per display nel targeting-info panel
    // this.currentActingPg = null; // Resettato solo se necessario o dopo l'azione
  }


  public getSelectedTargetNames(): string {
    return this.selectedTargets.map(target => target.name).join(', ');
  }

  private proceedToNextStep(): void {
    if (this.isPaused || !this.gameState || this.battleOutcome) {
      this.isLoading = false;
      this.cdRef.detectChanges();
      return;
    }
    // È importante che this.gameState sia quello aggiornato prima di chiamare checkBattleOver
    this.combatService.checkBattleOver(this.gameState).subscribe({
      next: (outcome) => {
        if (outcome !== "La battaglia è ancora in corso") {
          this.battleOutcome = outcome;
          this.addLogEntry(outcome);
          this.isLoading = false;
          sessionStorage.removeItem(ArenaComponent.PG_IDS_KEY);
        } else {
          this.advanceTurn();
        }
        this.cdRef.detectChanges();
      },
      error: (err) => {
        this.addLogEntry(`Errore nel controllare la fine della battaglia: ${err.message}`);
        this.isLoading = false;
        this.cdRef.detectChanges();
      }
    });
  }

  private advanceTurn(): void {
    if (this.isPaused || !this.gameState || this.battleOutcome) return;
    this.isLoading = true;
    this.cdRef.detectChanges();
    // Assicurati che this.gameState sia quello corretto e aggiornato
    this.combatService.advanceToNextEntity(this.gameState).subscribe({
      next: (nextStateFromServer) => {
        this.gameState = this.processIncomingGameState(nextStateFromServer, this.gameState);
        const nextChar = this.getCharacterById(this.gameState.currentEntity);
        this.addLogEntry(`Prossimo turno: ${nextChar?.name || 'Sconosciuto'}`);
        this.isLoading = false;
        // Qui resetActionState() potrebbe essere appropriato se non fatto altrove
        // this.resetActionState(); // Potrebbe essere necessario per pulire lo stato del turno precedente
        this.cdRef.detectChanges();
        this.checkIfMonsterTurn();
      },
      error: (err) => {
        this.addLogEntry(`Errore nell'avanzare il turno: ${err.message}`);
        this.isLoading = false;
        this.cdRef.detectChanges();
      }
    });
  }


  public getEntityType(entityId: number): 'pg' | 'monster' | 'unknown' {
    if (!this.gameState) return 'unknown';
    if (this.gameState.good.some(pg => pg.id === entityId)) return 'pg';
    if (this.gameState.evil.some(m => m.id === entityId)) return 'monster';
    return 'unknown';
  }

  public navigateToHome(): void {
    this.router.navigate(['/']);
  }

  public navigateToSelection(): void {
    this.router.navigate(['/selezione']);
  }

  public togglePauseMenu(): void {
    if (this.battleOutcome) return;
    this.isPaused = !this.isPaused;
    if (this.isPaused) {
      this.addLogEntry("Partita in pausa.");
      this.cancelTargeting(); // Annulla anche il targeting
    } else {
      this.addLogEntry("Partita ripresa.");
      this.checkIfMonsterTurn(); // Controlla se è il turno di un mostro alla ripresa
    }
    this.cdRef.detectChanges();
  }


  public startNewGameFromPause(): void {
    this.isPaused = false; // Assicurati che la pausa sia disattivata
    sessionStorage.removeItem(ArenaComponent.PG_IDS_KEY);
    this.navigateToSelection();
  }

  public navigateToHomeFromPause(): void {
    this.isPaused = false; // Assicurati che la pausa sia disattivata
    sessionStorage.removeItem(ArenaComponent.PG_IDS_KEY);
    this.navigateToHome();
  }

  ngOnDestroy(): void {
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
    // Considera di pulire sessionStorage qui se non vuoi che persista tra sessioni del browser
    // sessionStorage.removeItem(ArenaComponent.PG_IDS_KEY);
  }

  public getLogReversed()
  {
    return [...this.battleLog].reverse();
  }
}
