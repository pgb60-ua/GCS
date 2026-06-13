import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { CarDetailPage } from './car-detail.page';
import { CarDetailRoutingModule } from './car-detail-routing.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [CommonModule, IonicModule, SharedModule, CarDetailRoutingModule],
  declarations: [CarDetailPage]
})
export class CarDetailModule {}
