import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IChat, NewChat } from '../chat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChat for edit and NewChatFormGroupInput for create.
 */
type ChatFormGroupInput = IChat | PartialWithRequiredKeyOf<NewChat>;

type ChatFormDefaults = Pick<NewChat, 'id'>;

type ChatFormGroupContent = {
  id: FormControl<IChat['id'] | NewChat['id']>;
  dateChat: FormControl<IChat['dateChat']>;
  message: FormControl<IChat['message']>;
  stateChat: FormControl<IChat['stateChat']>;
  match: FormControl<IChat['match']>;
};

export type ChatFormGroup = FormGroup<ChatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChatFormService {
  createChatFormGroup(chat: ChatFormGroupInput = { id: null }): ChatFormGroup {
    const chatRawValue = {
      ...this.getFormDefaults(),
      ...chat,
    };
    return new FormGroup<ChatFormGroupContent>({
      id: new FormControl(
        { value: chatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateChat: new FormControl(chatRawValue.dateChat),
      message: new FormControl(chatRawValue.message),
      stateChat: new FormControl(chatRawValue.stateChat),
      match: new FormControl(chatRawValue.match),
    });
  }

  getChat(form: ChatFormGroup): IChat | NewChat {
    return form.getRawValue() as IChat | NewChat;
  }

  resetForm(form: ChatFormGroup, chat: ChatFormGroupInput): void {
    const chatRawValue = { ...this.getFormDefaults(), ...chat };
    form.reset(
      {
        ...chatRawValue,
        id: { value: chatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ChatFormDefaults {
    return {
      id: null,
    };
  }
}
