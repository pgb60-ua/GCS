import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '../../shared/shared.module';
import { GaragePageRoutingModule } from './garage-routing.module';
import { GaragePage } from './garage.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    GaragePageRoutingModule,
  ],
  declarations: [GaragePage],
})
export class GaragePageModule {}
