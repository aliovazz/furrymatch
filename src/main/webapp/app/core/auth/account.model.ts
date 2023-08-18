export class Account {
  constructor(
    public id: number,
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public firstName: string,
    public langKey: string,
    public lastName: string,
    public user_id: number,
    public login: string,
    public imageUrl: string,
    public secondName: string | null,
    public firstLastName: string,
    public secondLastName: string,
    public phoneNumber: number,
    public photo: string,
    public identityNumber: string,
    public address: string,
    public province: string,
    public canton: string,
    public district: string
  ) {}
}
