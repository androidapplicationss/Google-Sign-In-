package com.bhavesh.googlesigninproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ImageView imgProfile;
    private TextView txtName,txtEmail,txtId;
    private Button btnSignout;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgProfile = findViewById(R.id.imgProfile);
        txtEmail = findViewById(R.id.txtEmail);
        txtId = findViewById(R.id.txtId);
        txtName = findViewById(R.id.txtname);
        btnSignout = findViewById(R.id.btnSignOut);
         googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
         googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

         btnSignout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                     @Override
                     public void onResult(@NonNull Status status) {
                         if(status.isSuccess())
                         {
                             startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                             finish();
                         }
                         else
                             {
                                 Toast.makeText(ProfileActivity.this, "logout failed", Toast.LENGTH_SHORT).show();
                             }
                     }
                 });
             }
         });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    private void handleSigninResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            txtName.setText(account.getDisplayName());
            txtEmail.setText(account.getEmail());
            txtId.setText(account.getId());

            Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(imgProfile);
        }
        else
            {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();
            }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if(opr.isDone())
        {
            GoogleSignInResult googleSignInResult = opr.get();
            handleSigninResult(googleSignInResult);
        }
        else
            {
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult result) {
                        handleSigninResult(result);
                    }
                });
            }
    }
}