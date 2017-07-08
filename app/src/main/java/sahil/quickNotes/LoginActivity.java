package sahil.quickNotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText pinInput;
    String originalPin;
    PreferenceHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        handler=new PreferenceHandler(this);
        String status=handler.getPreferences(PreferenceHandler.loginStatus);
        if(status!=null && (status.compareTo(getResources().getString(R.string.login_state_true))==0))
        {
            /**
             * If the  user has already logged in directly take to home page.
             */
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        setPinFieldListener();
        setLoginButtonListener();
    }

    private void init() {
        login= (Button) findViewById(R.id.login);
        pinInput=(EditText) findViewById(R.id.pinLogin);
        originalPin=handler.getPreferences(PreferenceHandler.pin);
    }

    private void setPinFieldListener() {
        pinInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.setFirstLogin();
                pinInput.setBackground(getResources().getDrawable(R.drawable.shape_rectangular_border));
            }
        });

    }

    private void setLoginButtonListener() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin=pinInput.getText().toString();
                if(pin.compareTo(originalPin)!=0)
                {
                    Toast.makeText(getApplicationContext(), R.string.invalid_pin_text,Toast.LENGTH_SHORT).show();
                }
                else {
                    actionLogin();
                }
            }
        });
    }

    private void actionLogin() {
        handler.setPreference(PreferenceHandler.loginStatus,getResources().getString(R.string.login_state_true));
        Toast.makeText(getApplicationContext(), R.string.login_status_text,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        finish();
    }
}
