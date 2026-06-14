import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '../../shared/shared.module';
import { SolicitudesPageRoutingModule } from './solicitudes-routing.module';
import { SolicitudesPage } from './solicitudes.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    SolicitudesPageRoutingModule,
  ],
  declarations: [SolicitudesPage],
})
export class SolicitudesPageModule {}
