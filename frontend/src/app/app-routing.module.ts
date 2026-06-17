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
    path: 'checkout/:cocheId',
    loadChildren: () => import('./pages/checkout/checkout.module').then(m => m.CheckoutPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'compras',
    loadChildren: () => import('./pages/purchases/purchases.module').then(m => m.PurchasesPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'coche-comprado/:id',
    loadChildren: () => import('./pages/purchased-car/purchased-car.module').then(m => m.PurchasedCarPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'perfil',
    loadChildren: () => import('./pages/profile/profile.module').then(m => m.ProfilePageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'solicitudes',
    loadChildren: () => import('./pages/solicitudes/solicitudes.module').then(m => m.SolicitudesPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'admin',
    loadChildren: () => import('./pages/admin/admin.module').then(m => m.AdminPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/solicitudes',
    redirectTo: 'admin',
    pathMatch: 'full'
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
