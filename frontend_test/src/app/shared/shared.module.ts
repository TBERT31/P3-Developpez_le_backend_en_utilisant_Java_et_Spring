import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OwnerInfoComponent } from './components/owner-info/owner-info.component';

@NgModule({
  declarations: [
    OwnerInfoComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    OwnerInfoComponent
  ],
})
export class SharedModule { }
