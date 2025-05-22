import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule, NgFor, NgIf } from "@angular/common"; // NgFor e NgIf sono spesso in CommonModule, ma esplicitarli non fa male
import { RouterLink } from '@angular/router'; // Importa RouterLink
import { Subject, of } from 'rxjs';
import { takeUntil, catchError } from 'rxjs/operators';

import { Pg } from '../../../model/Pg'; // Assicurati che il percorso sia corretto
import { Monster } from '../../../model/Monster'; // Assicurati che il percorso sia corretto
import { PgRepositoryService } from '../../../services/pg-repository.service'; // Assicurati che il percorso sia corretto
import { MonsterRepositoryService } from '../../../services/monster-repository.service';
import {ActionDetailDto} from '../../../model/frontend-models'; // Assicurati che il percorso sia corretto

interface PgWithState extends Pg {
  id: number; // Assumendo che Pg abbia un id, o aggiungilo se necessario
  flipped: boolean;
  descriptionToShow: string | null;
  detailedActions?: ActionDetailDto[];
}

interface MonsterWithState extends Monster {
  id: number; // Assumendo che Monster abbia un id, o aggiungilo se necessario
  flipped: boolean;
  descriptionToShow: string | null;
}

@Component({
  selector: 'app-glossary',
  standalone: true,
  imports: [
    CommonModule, // Contiene *ngIf, *ngFor, ecc.
    RouterLink    // Aggiungi RouterLink qui
  ],
  templateUrl: './glossary.component.html',
  styleUrls: ['./glossary.component.css'] // Corretto da styleUrl a styleUrls
})
export class GlossaryComponent implements OnInit, OnDestroy {

  personaggi: PgWithState[] = [];
  mostri: MonsterWithState[] = [];
  errorLoadingPgs: string | null = null;
  errorLoadingMonsters: string | null = null;

  personaggiVisible: boolean = false;
  mostriVisible: boolean = false;

  private destroy$ = new Subject<void>();

  constructor(
    private pgRepo: PgRepositoryService,
    private monsterRepo: MonsterRepositoryService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadPersonaggi();
    this.loadMostri();
  }

  loadPersonaggi(): void {
    this.errorLoadingPgs = null;
    this.pgRepo.getAllPgs().pipe(
      takeUntil(this.destroy$),
      catchError(error => {
        console.error("Errore caricamento personaggi:", error); // Log dell'errore
        this.errorLoadingPgs = "Impossibile caricare i personaggi. Riprova più tardi.";
        this.cdRef.detectChanges();
        return of([]);
      })
    ).subscribe((resp: Pg[]) => {
      this.personaggi = resp.map((p, index): PgWithState => ({
        ...p,
        id: p.id !== undefined ? p.id : index, // Usa l'id esistente o un fallback
        flipped: false,
        descriptionToShow: null,
      }));
      this.cdRef.detectChanges();
    });
  }

  loadMostri(): void {
    this.errorLoadingMonsters = null;
    this.monsterRepo.getAllMonsters().pipe(
      takeUntil(this.destroy$),
      catchError(error => {
        console.error("Errore caricamento mostri:", error); // Log dell'errore
        this.errorLoadingMonsters = "Impossibile caricare i mostri. Riprova più tardi.";
        this.cdRef.detectChanges();
        return of([]);
      })
    ).subscribe((resp: Monster[]) => {
      this.mostri = resp.map((m, index): MonsterWithState => ({
        ...m,
        id: m.id !== undefined ? m.id : index, // Usa l'id esistente o un fallback
        flipped: false,
        descriptionToShow: null,
      }));
      this.cdRef.detectChanges();
    });
  }

  togglePersonaggi(): void {
    this.personaggiVisible = !this.personaggiVisible;
    this.cdRef.detectChanges();
  }

  toggleMostri(): void {
    this.mostriVisible = !this.mostriVisible;
    this.cdRef.detectChanges();
  }

  handlePgCardFrontClick(pg: PgWithState): void {
    if (!pg.flipped) {
      pg.descriptionToShow = pg.description || 'Nessuna descrizione generale disponibile.';
      pg.flipped = true;
      this.cdRef.detectChanges();
    }
  }

  showPgActionDetails(pg: PgWithState, actionDetail: ActionDetailDto, index: number): void {
    const description = actionDetail.description;
    pg.descriptionToShow = description;
    if (!pg.flipped) {
      pg.flipped = true;
    }
    this.cdRef.detectChanges();
  }

  showPgEquipmentDetails(pg: PgWithState, equipmentName: string, index: number): void {
    const description = (pg.equipmentsDescriptions && pg.equipmentsDescriptions[index])
      ? pg.equipmentsDescriptions[index]
      : `Dettagli per equipaggiamento '${equipmentName}' non disponibili.`;
    pg.descriptionToShow = description;
    if (!pg.flipped) {
      pg.flipped = true;
    }
    this.cdRef.detectChanges();
  }

  hidePgDetails(pg: PgWithState): void {
    if (pg.flipped) {
      pg.flipped = false;
      pg.descriptionToShow = null;
      this.cdRef.detectChanges();
    }
  }

  handleMonsterCardFrontClick(monster: MonsterWithState): void {
    if (!monster.flipped) {
      monster.descriptionToShow = monster.description || 'Nessuna descrizione disponibile.';
      monster.flipped = true;
      this.cdRef.detectChanges();
    }
  }

  showMonsterActionDetails(monster: MonsterWithState, actionName: string, index: number): void {
    const description = (monster.actionsDescriptions && monster.actionsDescriptions[index])
      ? monster.actionsDescriptions[index]
      : `Dettagli per azione '${actionName}' non disponibili.`;
    monster.descriptionToShow = description;
    if (!monster.flipped) {
      monster.flipped = true;
    }
    this.cdRef.detectChanges();
  }

  hideMonsterDetails(monster: MonsterWithState): void {
    if (monster.flipped) {
      monster.flipped = false;
      monster.descriptionToShow = null;
      this.cdRef.detectChanges();
    }
  }

  // Assicurati che PgWithState e MonsterWithState abbiano una proprietà 'id' univoca
  trackByPgId(index: number, item: PgWithState): number {
    return item.id;
  }

  trackByMonsterId(index: number, item: MonsterWithState): number {
    return item.id;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
