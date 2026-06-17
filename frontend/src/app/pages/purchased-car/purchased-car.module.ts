import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '../../shared/shared.module';
import { PurchasedCarPageRoutingModule } from './purchased-car-routing.module';
import { PurchasedCarPage } from './purchased-car.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    PurchasedCarPageRoutingModule,
  ],
  declarations: [PurchasedCarPage],
})
export class PurchasedCarPageModule {}
