package com.example.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginRegisterActivity extends AppCompatActivity {

    private static final String TAG = "LoginRegisterActivity";
    int AUTHUI_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            this.finish();
        }
    }

    public void handleLoginRegister(View view) {

        List<AuthUI.IdpConfig> provider = Arrays.asList(

                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
      /*optional*/.setTosAndPrivacyPolicyUrls("https://example.com","https://example.com")
                .setLogo(R.drawable.a)
                .setAlwaysShowSignInMethodScreen(true)
                .build();
      startActivityForResult(intent,AUTHUI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHUI_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                // we have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: "+user.getEmail());
                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp())
                {
                    Toast.makeText(this, "Welcome new user", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Welcome back again", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                this.finish();
            }
            else
            {
                //signing failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null)
                {
                    Log.d(TAG,"onActivityResult: the user has cancelled the sign in request");
                }
                else
                {
                    Log.d(TAG,"onActivityResult: "+response.getError());
                }
            }
        }
    }
}
