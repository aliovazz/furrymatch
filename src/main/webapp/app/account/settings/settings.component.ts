import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { RegisterService } from '../register/register.service';
import { OwnerService } from '../../entities/owner/service/owner.service';
import Swal from 'sweetalert2';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { LANGUAGES } from 'app/config/language.constants';
import { LoginService } from '../../login/login.service';
import { SessionStorageService } from 'ngx-webstorage';
import { ProfileService } from '../../layouts/profiles/profile.service';
import { Router } from '@angular/router';

const initialAccount: Account = {} as Account;

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['../register/register.component.css'],
})
export class SettingsComponent implements OnInit {
  success = false;
  languages = LANGUAGES;
  files: File[] = [];

  currentPhotoUrl: string | null = null;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;

  selectedProvinceId: number | null = null;
  selectedCantonId: number | null = null;
  provinces: any;
  cantones: any;
  districts: any;

  settingsForm = new FormGroup({
    email: new FormControl(initialAccount.email, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    secondName: new FormControl('', {
      nonNullable: false,
    }),
    firstLastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    secondLastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    phoneNumber: new FormControl(0, {
      nonNullable: true,
      validators: [Validators.required],
    }),
    identityNumber: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    address: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    province: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    canton: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    district: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    langKey: new FormControl(initialAccount.langKey, { nonNullable: true }),
    activated: new FormControl(initialAccount.activated, { nonNullable: true }),
    authorities: new FormControl(initialAccount.authorities, { nonNullable: true }),
    imageUrl: new FormControl(initialAccount.imageUrl, { nonNullable: true }),
    login: new FormControl(initialAccount.login, { nonNullable: true }),
  });

  selected = '';
  userId: any;
  photo: FormControl = new FormControl('');

  constructor(
    private accountService: AccountService,
    private translateService: TranslateService,
    private registerService: RegisterService,
    private ownerService: OwnerService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.userId = account.id;
        this.settingsForm.controls.login.setValue(account.login);
        this.settingsForm.controls.email.setValue(account.email);
      }
    });

    this.ownerService.find(this.userId).subscribe(owner => {
      if (owner) {
        this.settingsForm.controls.firstName.setValue(String(owner.body?.firstName));
        this.settingsForm.controls.secondName.setValue(String(owner.body?.secondName));
        this.settingsForm.controls.firstLastName.setValue(String(owner.body?.firstLastName));
        this.settingsForm.controls.secondLastName.setValue(String(owner.body?.secondLastName));
        this.settingsForm.controls.identityNumber.setValue(String(owner.body?.identityNumber));
        this.settingsForm.controls.address.setValue(String(owner.body?.address));
        this.settingsForm.controls.phoneNumber.setValue(Number(owner.body?.phoneNumber));
        this.settingsForm.controls.province.setValue(String(owner.body?.province));
        this.settingsForm.controls.canton.setValue(String(owner.body?.canton));
        this.settingsForm.controls.district.setValue(String(owner.body?.district));

        this.getCantones(Number(owner.body?.province));
        this.getDistricts(Number(owner.body?.province), Number(owner.body?.canton));

        if (owner.body?.photo) {
          this.currentPhotoUrl = String(owner.body?.photo);
          const fileName = owner.body.photo.split('/').pop() || 'user_photo.jpeg';
          this.loadExistingImage(this.currentPhotoUrl, fileName);
        }
      }
    });
    this.registerService.getProvinces().subscribe((response: any) => {
      const provincesArray = Object.entries(response).map(([id, name]) => ({ id, name }));
      this.provinces = provincesArray;
    });
  }

  loadExistingImage(url: string, fileName: string): void {
    this.registerService.downloadImage(url).subscribe(response => {
      const blob = new Blob([response], { type: 'image/jpeg' });
      const file = new File([blob], fileName, { type: 'image/jpeg' });
      this.files = [file];
    });
  }

  update(e: any) {
    this.selected = e.target.value;
  }

  updateProvince(e: any) {
    const selectedProvinceId = parseInt(e.target.value);
    if (selectedProvinceId) {
      this.selectedProvinceId = selectedProvinceId;
      this.getCantones(selectedProvinceId);
    } else {
      this.selectedProvinceId = null;
      this.selectedCantonId = null;
      this.cantones = null;
      this.districts = null;
    }
  }

  updateCanton(e: any) {
    const selectedCantonId = parseInt(e.target.value);
    if (selectedCantonId) {
      this.selectedCantonId = selectedCantonId;
      this.getDistricts(this.selectedProvinceId, selectedCantonId);
    } else {
      this.selectedCantonId = null;
      this.districts = null;
    }
  }

  getCantones(provinceId: number) {
    this.registerService.getCantones(provinceId).subscribe((response: any) => {
      const cantonesArray = Object.entries(response).map(([id, name]) => ({ id: parseInt(id), name }));
      this.cantones = cantonesArray;
    });
  }

  getDistricts(provinceId: number | null, cantonId: number) {
    this.registerService.getDistricts(provinceId, cantonId).subscribe((response: any) => {
      const districtsArray = Object.entries(response).map(([id, name]) => ({ id: parseInt(id), name }));
      this.districts = districtsArray;
    });
  }

  onSelect(event?: any) {
    console.log(event);
    this.files.push(...event.addedFiles);
  }

  onRemove(event?: any) {
    console.log(event);
    this.files.splice(this.files.indexOf(event), 1);
  }

  onUpload() {
    if (!this.files[0])
      Swal.fire({
        title: 'Error',
        text: 'Debés primero arrastrar o seleccionar una imagen.',
        icon: 'error',
        confirmButtonColor: '#3381f6',
        confirmButtonText: 'Cerrar',
      });

    const file_data = this.files[0];
    const data = new FormData();
    data.append('file', file_data);
    data.append('upload_preset', 'furry_match');
    data.append('cloud_name', 'alocortesu');

    this.registerService.uploadImage(data).subscribe(response => {
      if (response) {
        const secureUrl = response.secure_url;
        this.photo.setValue(secureUrl);
        Swal.fire({
          title: 'Fotografía agregada',
          text: 'Continuá registrando tus datos.',
          icon: 'success',
          confirmButtonColor: '#3381f6',
          confirmButtonText: 'Cerrar',
        });
      }
    });
  }

  save(): void {
    this.success = false;

    const account = this.settingsForm.getRawValue();
    let photoToSave = this.photo.value;

    if (this.currentPhotoUrl && this.files.length > 0 && this.files[0].name === this.currentPhotoUrl.split('/').pop()) {
      photoToSave = this.currentPhotoUrl;
    }

    this.ownerService.update({ user_id: this.userId, ...account, photo: photoToSave }).subscribe({
      next: () =>
        Swal.fire({
          title: 'Cambio exitoso',
          text: 'Se cambiaron exitosamente tus datos.',
          icon: 'success',
          confirmButtonColor: '#3381f6',
          confirmButtonText: 'Cerrar',
        }).then((result: any) => {
          this.router.navigate(['/pet']);
        }),
      error: response =>
        Swal.fire({
          title: 'Error',
          text: 'Hubo un error cambiando tus datos; intenta nuevamente..',
          icon: 'error',
          confirmButtonColor: '#3381f6',
          confirmButtonText: 'Cerrar',
        }),
    });
  }

  delete(): void {
    Swal.fire({
      title: '¿Deseás salir de FurryMatch?',
      text: 'Si hacés click en el botón de Sí perderás tu información personal y la de tus mascotas.',
      showCancelButton: false,
      showConfirmButton: true,
      showDenyButton: true,
      confirmButtonText: 'Sí',
      denyButtonText: 'No',
      icon: 'success',
      confirmButtonColor: '#3381f6',
      denyButtonColor: '#3381f6',
    }).then((result: any) => {
      if (result.isConfirmed) {
        this.ownerService.delete(this.userId).subscribe(() => {
          this.loginService.logout();
          this.router.navigate(['']);
          console.log('borrado');
        });
      } else if (result.isDenied) {
      }
    });
  }
}
