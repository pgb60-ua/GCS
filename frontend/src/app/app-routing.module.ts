import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./pages/login/login.module').then(m => m.LoginPageModule)
  },
  {
    path: 'registro',
    loadChildren: () => import('./pages/register/register.module').then(m => m.RegisterPageModule)
  },
  {
    path: 'catalogo',
    loadChildren: () => import('./home/home.module').then(m => m.HomePageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'coches/:id',
    loadChildren: () => import('./pages/car-detail/car-detail.module').then(m => m.CarDetailPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'garaje',
    loadChildren: () => import('./pages/garage/garage.module').then(m => m.GaragePageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'personalizar/:id',
    loadChildren: () => import('./pages/customize/customize.module').then(m => m.CustomizePageModule),
    canActivate: [AuthGuard]
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'home',
    redirectTo: 'catalogo',
    pathMatch: 'full'
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
