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

        this.run();
    }

    private void run(){
        loginButton.setOnClickListener(event -> {
            /*
            Durch Polymorphie resultieren alle Anfrage in einer ApiRequest. Darauf müsst ihr nur noch .doRequest() aufrufen, damit
            die Anfrage auch ausgeführt wird.
             */

            // Die Login Anfrage nimmt als Parameter den Text aus der Benutzername und Password Box
            ApiRequest request = new LoginRequest(username.getText().toString(), password.getText().toString(), response -> {

                // wenn die antwort null ist, dann konnte keine Verbindung zum Server hergestellt werden.
                if (response == null){
                    Utils.showServerConnectionError(instance);
                    return;
                }

                // wenn der HTTP-Code nicht HTTPCodes.OK ist, dann war die Verbindung fehlerhaft.
                if (response.getHttpCode() != HttpCodes.OK){
                    Utils.showErrorMessage(instance, "Ein unerwarteter Netzwerkfehler ist aufgetreten!", "Netzwerk code: " + response.getHttpCode().getCode() + "\n" + "Body: " + response.getJson().toString());
                    return;
                }

                // Der Server antwortet im JSON format. Wenn dies unvollständig ist, damit nicht dekodiert werden konnte, benarichtigen wir den Benutzer.
                if (response.getJson() == null){
                    Utils.showErrorMessage(instance, "Die Antwort des Servers konnte nicht dekodiert werden!", "");
                    return;
                }

                // Hier überprüfen wir ob der API-Code auch ok ist, also ob alles geklappt hat
                if (response.getApiCode() == ApiCodes.SUCCESS){
                    instance.runOnUiThread(() -> Toast.makeText(instance, "Erfolgreich angemeldet!", Toast.LENGTH_SHORT).show());

                    /*

                    Wenn alles richtig verlaufen ist, antwortet der Server mit folgender JSON struktur.
                    Der Token wird dazu verwendet um jede weitere Anfrage an den Server zu machen.

                    {
                    "token": token,
                    "code": APICodes.SUCCESS
                    }
                     */

                    // hier speichern wir den Token im StateManager. Das ist einfach nur eine Klasse
                    // die globale Sachen, wie z.B. den Token, speichert.
                    try {
                        StateManager.getInstance().setToken(response.getJson().getString("token"));
                    } catch (JSONException e) {
                        Utils.showErrorMessage(instance, "Es ist ein Fehler beim Auslesen des Tokens aufgetreten!", e.getMessage());
                        return;
                    }

                    this.onSuccessfulLogin(response);

                } else if (response.getApiCode() == ApiCodes.BAD_USERNAME_OR_PASSWORD) {
                    Utils.showErrorMessage(instance, "Der Benutzername oder das Passwort ist falsch!", "");

                // Hier ist ein unbekannter Fehler aufgetreten. Hier wird dem Benutzer eine ausführlichere Fehlermeldung angezeigt.
                } else {
                    instance.runOnUiThread(() -> Utils.showErrorMessage(instance, "Anmeldung fehlgeschlagen!", "HTTP Code: " + response.getResponse().code() + "\nApi Code: " + response.getApiCode().getCode()));

                    System.out.println("Failed to make login request: " + response.getJson().toString() + "\nCode: " + response.getResponse().code() + "\nRoute: " + response.getResponse().request().url() + "\nMethod: " + response.getResponse().request().method());
                }
            });

            /*
            #######################################################################
            # Das hier ist ganz wichtig, sonst wird euere Anfrage nie ausgeführt! #
            #######################################################################
             */
            request.doRequest();
        });

        // wenn der Benutzer auf den Konto erstellen Text drückt, zeigen wir ihm den Konto-erstell-Screen
        // und lassen die CreateAccountScreen klasse die Logik dahinter übernehmen
        createAccount.setOnClickListener(event -> {
            instance.setContentView(R.layout.create_account_screen);
            new CreateAccountScreen();
        });
    }

    // das hier habe ich erstellt, damit man sehen kann, was nach dem Erfolgreichem Login getan wird.
    private void onSuccessfulLogin(ApiResponse response){
        instance.runOnUiThread(() -> instance.setContentView(R.layout.tobi_ui));
        new QuizScreen();
    }

}
