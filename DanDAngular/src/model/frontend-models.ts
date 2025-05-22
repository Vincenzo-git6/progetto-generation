// Questo enum rimane invariato
export enum ActionType {
  BASE = 'BASE',
  HEAVY = 'HEAVY',
  SPECIALE = 'SPECIALE',
}

// NUOVA INTERFACCIA per i dettagli dell'azione
export interface ActionDetailDto {
  name: string;
  description: string;
  actionType: ActionType; // Il tipo di azione (BASE, HEAVY, SPECIALE)
  currentCooldown: number; // Cooldown attuale dell'azione (0 se utilizzabile)
  maxCooldown: number;     // Cooldown massimo/base dell'azione
  maxTargets: number;      // Numero massimo di bersagli (es. 1, 3, o un valore speciale per "tutti")
  targetAllies: boolean;  // True se l'azione può bersagliare alleati (es. cure)
  targetSelf: boolean;    // True se l'azione bersaglia solo sé stesso e non richiede selezione
  // Aggiungi qui altre proprietà specifiche dell'azione se necessario
}

export interface PgDto {
  id: number;
  name: string;
  description: string;
  hp: number;
  atk: number;
  def: number;
  spd: number;
  detailedActions: ActionDetailDto[]; // NUOVO CAMPO con i dettagli completi delle azioni
  equipmentsName: string[];
  equipmentsDescriptions: string[];
  enumType: string;
  imageUrl: string;
  currentHp: number;
  maxHp: number;
}

export interface MonsterDto {
  id: number;
  name: string;
  description: string;
  danger: string;
  hp: number;
  atk: number;
  def: number;
  spd: number;
  actionsName: string[];
  actionsDescriptions: string[];
  // detailedActions?: ActionDetailDto[]; // Se necessario per i mostri
  imageUrl: string;
  currentHp: number;
  maxHp: number;
}

export interface GameStateDto {
  good: PgDto[];
  evil: MonsterDto[];
  order: number[];
  currentEntity: number;
}

export interface ActionRequest {
  previousDto: GameStateDto;
  target: number[];
  actionType: ActionType;
  actionName?: string;
}
