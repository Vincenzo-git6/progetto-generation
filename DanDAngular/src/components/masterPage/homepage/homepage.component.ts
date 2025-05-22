import { Component, Renderer2, ElementRef } from '@angular/core'; // Aggiunto Renderer2 e ElementRef
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { NgIf, NgClass } from '@angular/common'; // Aggiunto NgClass

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [
    RouterLink,
    NgIf,
    NgClass
  ],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent {
  showCreditsModal: boolean = false;

  constructor(private http: HttpClient, private renderer: Renderer2, private el: ElementRef) {
  }

  newGame() {

  }

  openCreditsModal(): void {
    this.showCreditsModal = true;
  }

  closeCreditsModal(): void {
    this.showCreditsModal = false;
  }
}
