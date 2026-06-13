import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CarService } from '../../core/services/car.service';
import { Car } from '../../core/models/car.model';

@Component({
  selector: 'app-car-detail',
  templateUrl: './car-detail.page.html',
  styleUrls: ['./car-detail.page.scss'],
  standalone: false,
})
export class CarDetailPage implements OnInit {
  car: Car | null = null;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private carService: CarService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.carService.getCarById(id).subscribe({
        next: (car) => { this.car = car; this.loading = false; },
        error: () => { this.error = true; this.loading = false; }
      });
    }
  }
}
