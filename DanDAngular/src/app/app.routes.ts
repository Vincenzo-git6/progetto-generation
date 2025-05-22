import { Routes } from '@angular/router';
import { HomepageComponent } from '../components/masterPage/homepage/homepage.component';
import { SelectorPgComponent } from '../components/gameArch/selector-pg/selector-pg.component';
import { GlossaryComponent } from '../components/gameArch/glossary/glossary.component';
import { ActionPanelComponent } from '../components/gameArch/action-panel/action-panel.component';
import { ArenaComponent } from '../components/gameArch/arena/arena.component'; // Importazione ArenaComponent

export const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'selezione', component: SelectorPgComponent },
  { path: 'glossary', component: GlossaryComponent },
  { path: 'actions', component: ActionPanelComponent },
  { path: 'arena', component: ArenaComponent }, // NUOVA ROUTE PER L'ARENA
  { path: '**', redirectTo: '', pathMatch: 'full' } // Wildcard route
];
