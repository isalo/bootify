export class AuthenticationRequest {

  constructor(data:Partial<AuthenticationRequest>) {
    Object.assign(this, data);
  }

  email?: string|null;
  password?: string|null;

}

export class AuthenticationResponse {
  accessToken?: string;
}

export class RegistrationRequest {

  constructor(data:Partial<RegistrationRequest>) {
    Object.assign(this, data);
  }

  email?: string|null;
  password?: string|null;
  firstName?: string|null;
  lastName?: string|null;
  resetPasswordToken?: string|null;
  resetPasswordTokenSentAt?: string|null;
  emailConfirmationToken?: string|null;
  emailConfirmationTokenSentAt?: string|null;
  emailConfirmed?: boolean|null;
  countryCode?: string|null;
  phone?: string|null;
  phoneConfirmationToken?: string|null;
  phoneConfirmationTokenSentAt?: string|null;
  phoneConfirmed?: boolean|null;
  profileImageUrl?: string|null;
  isActive?: boolean|null;
  isLocked?: boolean|null;

}
