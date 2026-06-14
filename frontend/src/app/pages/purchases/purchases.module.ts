import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '../../shared/shared.module';
import { PurchasesPageRoutingModule } from './purchases-routing.module';
import { PurchasesPage } from './purchases.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    SharedModule,
    PurchasesPageRoutingModule,
  ],
  declarations: [PurchasesPage],
})
export class PurchasesPageModule {}
