package app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.api.state.StateManager;
import app.ui.LoginAccountScreen;
import de.uwuwhatsthis.quizApp.ui.loginScreen.R;

public class MainActivity extends AppCompatActivity {
    private StateManager stateManager;

    private static MainActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        INSTANCE = MainActivity.this;
        stateManager = StateManager.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new LoginAccountScreen();
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("insertUniqueIdHere", "Benarichtigungen für diese App!", importance);
            channel.setDescription("Eine beschreibung der benarichtigungen für diese App!");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static MainActivity getInstance(){
        return INSTANCE;
    }
}