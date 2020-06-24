package com.pedromihael.desmascarandoapp.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        // retornar que o usuario nao esta cadastrado
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        // retornar que a senha nao condiz com o usuario
        return passwordError;
    }

    boolean isDataValid() {
        // Verificar aqui no banco se existe um registro de login+senha conforme digitado
        return isDataValid;
    }
}