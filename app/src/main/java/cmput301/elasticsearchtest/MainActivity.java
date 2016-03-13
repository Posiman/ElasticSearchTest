package cmput301.elasticsearchtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Login> logins = new ArrayList<Login>();
    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
    }

    public void login(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String login_string = username + " " + password;
        Log.i("string", login_string);

        ElasticsearchLoginController.GetLoginsTask getLoginsTask =
                new ElasticsearchLoginController.GetLoginsTask();
        try {
            getLoginsTask.execute(login_string);
            logins = getLoginsTask.get();
        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        for (Login login : logins){
            Log.i("Test", login.toString());
        }
    }
}
