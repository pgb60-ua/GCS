import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '../../shared/shared.module';
import { CustomizePageRoutingModule } from './customize-routing.module';
import { CustomizePage } from './customize.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    CustomizePageRoutingModule,
  ],
  declarations: [CustomizePage],
})
export class CustomizePageModule {}
