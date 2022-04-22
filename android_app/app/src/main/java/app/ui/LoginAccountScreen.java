package app.ui;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import app.MainActivity;
import de.uwuwhatsthis.quizApp.ui.loginScreen.R;
import app.api.generics.ApiResponse;
import app.api.requests.account.LoginRequest;
import app.api.codes.ApiCodes;
import app.api.codes.HttpCodes;
import app.api.generics.ApiRequest;
import app.api.state.StateManager;
import app.ui.utils.Utils;

public class LoginAccountScreen {
    private TextView username, password, createAccount;
    private Button loginButton;

    private MainActivity instance;

    public LoginAccountScreen(){
        instance = MainActivity.getInstance();

        username = instance.findViewById(R.id.login_username);
        password = instance.findViewById(R.id.login_password);

        loginButton = instance.findViewById(R.id.login_button);

        createAccount = instance.findViewById(R.id.login_create_account);

//        instance.runOnUiThread(() -> instance.setContentView(R.layout.activity_main));

        this.run();
    }

    private void run(){
        loginButton.setOnClickListener(event -> {
            ApiRequest request = new LoginRequest(username.getText().toString(), password.getText().toString(), response -> {
                if (response == null){
                    Utils.showServerConnectionError(instance);
                    return;
                }

                if (response.getHttpCode() != HttpCodes.OK){
                    Utils.showErrorMessage(instance, "Ein unerwarteter Netzwerkfehler ist aufgetreten!", "Netzwerk code: " + response.getHttpCode().getCode() + "\n" + "Body: " + response.getJson().toString());
                    return;
                }

                if (response.getJson() == null){
                    Utils.showErrorMessage(instance, "Die Antwort des Servers konnte nicht dekodiert werden!", "");
                    return;
                }

                if (response.getApiCode() == ApiCodes.SUCCESS){ // request ok
                    instance.runOnUiThread(() -> Toast.makeText(instance, "Erfolgreich angemeldet!", Toast.LENGTH_SHORT).show());


                    try {
                        StateManager.getInstance().setToken(response.getJson().getString("token"));
                    } catch (JSONException e) {
                        Utils.showErrorMessage(instance, "Es ist ein Fehler beim Auslesen des Tokens aufgetreten!", e.getMessage());
                        return;
                    }

                    this.onSuccessfulLogin(response);

                } else if (response.getApiCode() == ApiCodes.BAD_USERNAME_OR_PASSWORD) {
                    Utils.showErrorMessage(instance, "Der Benutzername oder das Passwort ist falsch!", "");

                } else {
                    instance.runOnUiThread(() -> Utils.showErrorMessage(instance, "Anmeldung fehlgeschlagen!", "HTTP Code: " + response.getResponse().code() + "\nApi Code: " + response.getApiCode().getCode()));

                    System.out.println("Failed to make login request: " + response.getJson().toString() + "\nCode: " + response.getResponse().code() + "\nRoute: " + response.getResponse().request().url() + "\nMethod: " + response.getResponse().request().method());
                }
            });

            request.doRequest();
        });

        createAccount.setOnClickListener(event -> {
            instance.setContentView(R.layout.create_account_screen);
            new CreateAccountScreen();
        });
    }

    private void onSuccessfulLogin(ApiResponse response){
        instance.runOnUiThread(() -> instance.setContentView(R.layout.tobi_ui));
        new QuizScreen();
    }

}
