import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CarDetailPage } from './car-detail.page';

const routes: Routes = [
  { path: '', component: CarDetailPage }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CarDetailRoutingModule {}
