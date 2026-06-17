import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PurchasedCarPage } from './purchased-car.page';

const routes: Routes = [
  {
    path: '',
    component: PurchasedCarPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PurchasedCarPageRoutingModule {}
