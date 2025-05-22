import { Component, OnInit } from '@angular/core';
import { Pg } from '../../../model/Pg'; // Assicurati che questo percorso sia corretto per il tuo modello Pg
import { PgRepositoryService } from '../../../services/pg-repository.service'; // Assicurati che questo percorso sia corretto
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-selector-pg',
  standalone: true,
  imports: [
    CommonModule, // Include *ngFor, *ngIf, ecc.
    RouterLink
  ],
  templateUrl: './selector-pg.component.html',
  styleUrl: './selector-pg.component.css' // O styleUrls se hai più file CSS
})
export class SelectorPgComponent implements OnInit {
  allCharacters: Pg[] = [];
  idSelectedCharacter: number[] = [];
  errorLoadingPgs: string | null = null; // Proprietà per messaggi di errore

  private static readonly PG_IDS_KEY = 'selectedPgIds';

  constructor(
    private pgRepo: PgRepositoryService,
    private router: Router // Router iniettato per la navigazione
  ) {}

  ngOnInit(): void {
    this.pgRepo.getAllPgs()
      .subscribe({
        next: (data) => {
          // Assicurati che ogni personaggio abbia un 'id' numerico
          this.allCharacters = data.map(pg => ({ ...pg, id: pg.id ?? 0 }));
        },
        error: (err) => {
          console.error("Errore caricamento personaggi:", err);
          this.errorLoadingPgs = "Impossibile caricare i personaggi. Riprova più tardi.";
          this.allCharacters = [];
        },
      });
  }

  selectionChange(id: number): void {
    const index = this.idSelectedCharacter.indexOf(id);

    if (index > -1) {
      this.idSelectedCharacter.splice(index, 1);
    } else if (this.idSelectedCharacter.length < 3) {
      this.idSelectedCharacter.push(id);
    }
  }

  get selectedNames(): string {
    if (!this.allCharacters || this.allCharacters.length === 0) {
      return 'Nessuno';
    }
    return this.allCharacters
      .filter(c => this.idSelectedCharacter.includes(c.id))
      .map(c => c.name)
      .join(', ') || 'Nessuno';
  }

  // Metodo per tracciare gli elementi nella lista *ngFor
  trackByCharacterId(index: number, item: Pg): number {
    return item.id ?? index; // Usa l'ID del personaggio o l'indice come fallback
  }

  // Metodo per navigare all'arena
  goToArena(): void {
    if (this.idSelectedCharacter.length === 3) {
      // Salva gli ID in sessionStorage per recuperarli nell'ArenaComponent
      sessionStorage.setItem(SelectorPgComponent.PG_IDS_KEY, JSON.stringify(this.idSelectedCharacter));
      this.router.navigate(['/arena']);
    } else {
      console.warn('Devi selezionare 3 personaggi per iniziare.');
      // Qui potresti voler mostrare un messaggio all'utente nella UI
      // invece di usare console.warn o alert.
      // Ad esempio, impostando una proprietà e mostrandola nel template:
      // this.selectionError = 'Devi selezionare 3 personaggi per iniziare.';
    }
  }
}
