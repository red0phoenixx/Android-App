package app.ui;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.MainActivity;
import de.uwuwhatsthis.quizApp.ui.loginScreen.R;
import app.api.codes.ApiCodes;
import app.api.generics.ApiRequest;
import app.api.requests.account.CreateAccountRequest;
import app.ui.utils.Utils;

public class CreateAccountScreen {
    private final TextView username, password, passwordConfirmation;
    private final Button createAccountButton;

    private final MainActivity instance;

    public CreateAccountScreen(){
        instance = MainActivity.getInstance();

        username = instance.findViewById(R.id.create_account_username);
        password = instance.findViewById(R.id.create_account_password);
        passwordConfirmation = instance.findViewById(R.id.create_account_password_confirmation);

        createAccountButton = instance.findViewById(R.id.create_account_button);

        run();
    }

    private void run(){
        createAccountButton.setOnClickListener(event -> {
            String passwordText = password.getText().toString();
            String confirmationText = passwordConfirmation.getText().toString();

            if (!passwordText.equals(confirmationText)){
                instance.runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(instance);

                    builder.setCancelable(true);
                    builder.setTitle("Die Passwörter sind nicht gleich!");
                    builder.setIcon(R.drawable.ic_baseline_error_24);

                    builder.setNeutralButton("Ok", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    });

                    builder.show();
                });
                return;
            }

            ApiRequest request = new CreateAccountRequest(username.getText().toString(), password.getText().toString(), response -> {
                if (!response.couldConnect()){
                    Utils.showServerConnectionError(instance);
                    return;
                }

                if (response.getHttpCode().getCode() != 200){
                    Utils.showErrorMessage(instance, "Ein unerwarteter Netzwerkfehler ist aufgetreten!", "Netzwerk code: " + response.getResponse().code()  + "\n" + "Body: " + response.getJson().toString());
                    return;
                }

                if (response.getApiCode() == ApiCodes.SUCCESS){
                    instance.runOnUiThread(() -> {
                        Toast.makeText(instance, "Das Konto wurde erfolgreich erstellt!", Toast.LENGTH_SHORT).show();

                        instance.setContentView(R.layout.activity_main);
                        new LoginAccountScreen();
                    });

                } else if (response.getApiCode() == ApiCodes.ACCOUNT_ALREADY_EXISTS){
                    Utils.showErrorMessage(instance, "Ein Konto mit dem Benutzernamen existiert bereits!", "");

                } else {
                    Utils.showErrorMessage(instance, "Eine unerwartete Antwort vom server ist aufgetreten!", "Api code: " + response.getJson().optInt("code"));
                }
            });

            request.doRequest();
        });
    }
}
