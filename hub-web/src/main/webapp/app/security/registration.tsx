import React from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { RegistrationRequest } from 'app/security/authentication-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    email: yup.string().emptyToNull().max(255).required(),
    password: yup.string().emptyToNull().max(72).required(),
    firstName: yup.string().emptyToNull().max(255),
    lastName: yup.string().emptyToNull().max(255),
    resetPasswordToken: yup.string().emptyToNull().max(255),
    resetPasswordTokenSentAt: yup.string().emptyToNull().offsetDateTime(),
    emailConfirmationToken: yup.string().emptyToNull().max(255),
    emailConfirmationTokenSentAt: yup.string().emptyToNull().offsetDateTime(),
    emailConfirmed: yup.bool(),
    countryCode: yup.string().emptyToNull().max(10),
    phone: yup.string().emptyToNull().max(50),
    phoneConfirmationToken: yup.string().emptyToNull().max(255),
    phoneConfirmationTokenSentAt: yup.string().emptyToNull().offsetDateTime(),
    phoneConfirmed: yup.bool(),
    profileImageUrl: yup.string().emptyToNull().max(255),
    isActive: yup.bool(),
    isLocked: yup.bool()
  });
}

export default function Registration() {
  const { t } = useTranslation();
  useDocumentTitle(t('authentication.login.headline'));

  const navigate = useNavigate();

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const getMessage = (key: string) => {
    const messages: Record<string, string> = {
      USER_EMAIL_UNIQUE: t('registration.register.taken'),
      USER_EXTERNAL_LOGIN_UNIQUE: t('exists.user.externalLogin')
    };
    return messages[key];
  };

  const register = async (data: RegistrationRequest) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/register', data);
      navigate('/login', {
            state: {
              msgSuccess: t('registration.register.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t, getMessage);
    }
  };

  return (<>
    <h1 className="grow text-3xl md:text-4xl font-medium mb-8">{t('registration.register.headline')}</h1>
    <form onSubmit={useFormResult.handleSubmit(register)} noValidate>
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="email" required={true} />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="password" required={true} type="password" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="firstName" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="lastName" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="resetPasswordToken" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="resetPasswordTokenSentAt" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="emailConfirmationToken" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="emailConfirmationTokenSentAt" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="emailConfirmed" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="countryCode" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="phone" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="phoneConfirmationToken" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="phoneConfirmationTokenSentAt" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="phoneConfirmed" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="profileImageUrl" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="isActive" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="registrationRequest" field="isLocked" type="checkbox" />
      <input type="submit" value={t('registration.register.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
