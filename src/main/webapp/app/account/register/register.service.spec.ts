import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { RegisterService } from './register.service';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Registration } from './register.model';

describe('RegisterService Service', () => {
  let service: RegisterService;
  let httpMock: HttpTestingController;
  let applicationConfigService: ApplicationConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    service = TestBed.inject(RegisterService);
    applicationConfigService = TestBed.inject(ApplicationConfigService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('Service methods', () => {
    it('should call register endpoint with correct values', () => {
      // GIVEN
      const login = 'abc';
      const email = 'test@test.com';
      const password = 'pass';
      const langKey = 'FR';
      const firstName = '';
      const secondName = '';
      const firstLastName = '';
      const secondLastName = '';
      const phoneNumber = 8989989;
      const photo = '';
      const identityNumber = '';
      const address = '';
      const province = '';
      const canton = '';
      const district = '';
      const registration = new Registration(
        login,
        email,
        password,
        langKey,
        firstName,
        secondName,
        firstLastName,
        secondLastName,
        phoneNumber,
        photo,
        identityNumber,
        address,
        province,
        canton,
        district
      );

      // WHEN
      service.save(registration).subscribe();

      const testRequest = httpMock.expectOne({
        method: 'POST',
        url: applicationConfigService.getEndpointFor('api/register'),
      });

      // THEN
      expect(testRequest.request.body).toEqual({
        email,
        langKey,
        login,
        password,
        firstName,
        secondName,
        firstLastName,
        secondLastName,
        phoneNumber,
        identityNumber,
        address,
        province,
        canton,
        district,
      });
    });
  });
});
