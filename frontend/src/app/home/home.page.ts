import { Component, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Subscription, switchMap } from 'rxjs';
import { Car } from '../core/models/car.model';
import { AuthService } from '../core/services/auth.service';
import { CarService } from '../core/services/car.service';
import { User } from '../core/models/user.model';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false,
})
export class HomePage implements OnDestroy {
  currentUser: User | null = null;
  cars: Car[] = [];
  filteredCars: Car[] = [];
  teams: string[] = [];
  selectedTeam = '';
  searchQuery = '';
  loading = true;
  error = false;
  errorMessage = '';

  private loadTrigger$ = new Subject<void>();
  private sub: Subscription;

  constructor(
    private authService: AuthService,
    private carService: CarService,
    private router: Router
  ) {
    this.sub = this.loadTrigger$.pipe(
      switchMap(() => this.carService.getBaseCars())
    ).subscribe({
      next: (cars) => {
        this.cars = cars;
        this.teams = [...new Set(cars.map(c => c.equipoF1))].sort();
        this.applyFilters();
        this.loading = false;
        this.error = false;
        this.errorMessage = '';
      },
      error: (err) => {
        this.error = true;
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el catalogo.';
      }
    });
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  ionViewWillEnter(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadCars();
  }

  loadCars(): void {
    this.loading = true;
    this.error = false;
    this.errorMessage = '';
    this.loadTrigger$.next();
  }

  applyFilters(): void {
    const q = this.searchQuery.toLowerCase().trim();
    this.filteredCars = this.cars.filter(car => {
      const matchesTeam = !this.selectedTeam || car.equipoF1 === this.selectedTeam;
      const matchesSearch = !q ||
        car.nomenclatura.toLowerCase().includes(q) ||
        car.equipoF1.toLowerCase().includes(q) ||
        car.temporada.toLowerCase().includes(q);
      return matchesTeam && matchesSearch;
    });
  }

  selectTeam(team: string): void {
    this.selectedTeam = this.selectedTeam === team ? '' : team;
    this.applyFilters();
  }

  onSearchChange(event: CustomEvent): void {
    this.searchQuery = event.detail.value ?? '';
    this.applyFilters();
  }

  openCar(car: Car): void {
    this.router.navigate(['/coches', car.id]);
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('es-ES').format(price) + ' €';
  }
}
