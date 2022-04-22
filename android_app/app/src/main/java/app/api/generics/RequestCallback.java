package de.uwuwhatsthis.quizApp.ui.loginScreen.api.generics;

import okhttp3.Response;

public interface RequestCallback {

    void execute(ApiResponse response);
}
