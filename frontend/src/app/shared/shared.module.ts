import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { AppHeaderComponent } from './components/app-header/app-header.component';
import { EmptyStateComponent } from './components/empty-state/empty-state.component';
import { LoadingSpinnerComponent } from './components/loading-spinner/loading-spinner.component';

@NgModule({
  imports: [
    CommonModule,
    IonicModule,
  ],
  declarations: [
    AppHeaderComponent,
    EmptyStateComponent,
    LoadingSpinnerComponent,
  ],
  exports: [
    AppHeaderComponent,
    EmptyStateComponent,
    LoadingSpinnerComponent,
  ],
})
export class SharedModule {}
