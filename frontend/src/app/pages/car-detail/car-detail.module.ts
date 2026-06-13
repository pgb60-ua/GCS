import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '../../shared/shared.module';
import { CarDetailPageRoutingModule } from './car-detail-routing.module';
import { CarDetailPage } from './car-detail.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    CarDetailPageRoutingModule,
  ],
  declarations: [CarDetailPage],
})
export class CarDetailPageModule {}
