import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Registration } from './register.model';
import { AuthServerProvider } from 'app/core/auth/auth-session.service';
import { switchMap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class RegisterService {
  constructor(
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private authenticationService: AuthServerProvider
  ) {}

  downloadImage(url: string): Observable<any> {
    return this.http.get(url, { responseType: 'blob' });
  }

  uploadImage(vals: any): Observable<any> {
    const data = vals;
    return this.http.post('https://api.cloudinary.com/v1_1/alocortesu/image/upload', data);
  }

  getProvinces() {
    return this.http.get('https://ubicaciones.paginasweb.cr/provincias.json');
  }

  getCantones(provinceId: number) {
    return this.http.get(`https://ubicaciones.paginasweb.cr/provincia/${provinceId}/cantones.json`);
  }

  getDistricts(provinceId: number | null, cantonId: number) {
    return this.http.get(`https://ubicaciones.paginasweb.cr/provincia/${provinceId}/canton/${cantonId}/distritos.json`);
  }

  save(registration: Registration): Observable<{}> {
    return this.http.post(this.applicationConfigService.getEndpointFor('api/register'), registration, { observe: 'response' }).pipe(
      switchMap(() => {
        return this.authenticationService.login({
          username: registration.login,
          password: registration.password,
          rememberMe: true,
        });
      })
    );
  }
}
