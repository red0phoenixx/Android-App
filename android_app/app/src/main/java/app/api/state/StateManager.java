package de.uwuwhatsthis.quizApp.ui.loginScreen.api.state;

public class StateManager {

    private String token;

    private static StateManager instance;

    public StateManager(){

    }

    public static StateManager getInstance() {
        if (instance == null){
            instance = new StateManager();
        }

        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
