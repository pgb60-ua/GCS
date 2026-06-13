import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { CarService } from "../../core/services/car.service";
import { AuthService } from "../../core/services/auth.service";
import { Car } from "../../core/models/car.model";
import { Review } from "../../core/models/review.model";
import { ReviewSummary } from "../../core/models/review-summary.model";
import { User } from "../../core/models/user.model";
import { ReviewService } from "../../core/services/review.service";

@Component({
  selector: "app-car-detail",
  templateUrl: "./car-detail.page.html",
  styleUrls: ["./car-detail.page.scss"],
  standalone: false,
})
export class CarDetailPage {
  car: Car | null = null;
  carId = "";
  currentUser: User | null = null;
  loading = true;
  error = false;
  errorMessage = "";
  duplicating = false;

  reviews: Review[] = [];
  summary: ReviewSummary | null = null;
  reviewsLoading = true;

  puntuacion = 5;
  comentario = "";
  formError = "";
  formSuccess = "";
  submitLoading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private carService: CarService,
    private authService: AuthService,
    private reviewService: ReviewService,
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    const id = this.route.snapshot.paramMap.get("id");
    if (id) {
      this.carId = id;
      this.carService.getCarById(id).subscribe({
        next: (car) => {
          this.car = car;
          this.loading = false;
        },
        error: () => {
          this.error = true;
          this.loading = false;
        },
      });

      this.loadReviews(id);
    }
  }

  ionViewWillEnter(): void {
    this.error = false;
    this.errorMessage = "";
    const id = this.route.snapshot.paramMap.get("id");
    if (!id) {
      this.error = true;
      this.errorMessage = "ID de coche no valido.";
      return;
    }
    this.loading = true;
    this.carService.getCarById(id).subscribe({
      next: (car) => {
        this.loading = false;
        this.car = car;
      },
      error: (err) => {
        this.loading = false;
        this.error = true;
        this.errorMessage =
          err?.error?.message ?? "No se ha podido cargar el coche.";
      },
    });
  }

  personalize(): void {
    if (!this.car) return;
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl("/login", { replaceUrl: true });
      return;
    }
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigateByUrl("/login", { replaceUrl: true });
      return;
    }
    this.duplicating = true;
    this.carService.duplicateBaseCar(this.car.id, user.id).subscribe({
      next: (newCar) => {
        this.duplicating = false;
        this.router.navigateByUrl(`/personalizar/${newCar.id}`, {
          replaceUrl: true,
        });
      },
      error: (err) => {
        this.duplicating = false;
        this.error = true;
        this.errorMessage =
          err?.error?.message ?? "No se ha podido personalizar el coche.";
      },
    });
  }

  loadReviews(cocheId: string): void {
    this.reviewsLoading = true;
    this.reviewService.getReviewsByCar(cocheId).subscribe({
      next: (reviews) => {
        this.reviews = reviews;
        this.reviewsLoading = false;
      },
      error: () => {
        this.reviews = [];
        this.reviewsLoading = false;
      },
    });

    this.reviewService.getReviewSummary(cocheId).subscribe({
      next: (summary) => {
        this.summary = summary;
      },
      error: () => {
        this.summary = null;
      },
    });
  }

  publishReview(): void {
    this.formError = "";
    this.formSuccess = "";

    if (!this.currentUser) {
      this.formError = "Debes iniciar sesion para publicar una resenia.";
      return;
    }

    const comentario = this.comentario.trim();
    if (comentario.length < 3) {
      this.formError = "El comentario debe tener al menos 3 caracteres.";
      return;
    }

    this.submitLoading = true;
    this.reviewService
      .createReview(
        this.currentUser.id,
        this.carId,
        this.puntuacion,
        comentario,
      )
      .subscribe({
        next: () => {
          this.submitLoading = false;
          this.comentario = "";
          this.puntuacion = 5;
          this.formSuccess = "Resenia publicada.";
          this.loadReviews(this.carId);
        },
        error: (error) => {
          this.submitLoading = false;
          this.formError =
            error?.error?.message ?? "No se pudo publicar la resenia.";
        },
      });
  }

  getStars(rating: number): string {
    const rounded = Math.round(rating);
    const full = "★".repeat(Math.max(0, Math.min(5, rounded)));
    const empty = "☆".repeat(Math.max(0, 5 - rounded));
    return `${full}${empty}`;
  }
}
