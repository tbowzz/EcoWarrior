package com.ragnardev.ecowarrior.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ragnardev.ecowarrior.Model.ClientModel;
import com.ragnardev.ecowarrior.Persistence.Firebase.FirebasePersistence;
import com.ragnardev.ecowarrior.Persistence.IPersistence;
import com.ragnardev.ecowarrior.R;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, Observer
{
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private TextView mDetailTextView;

    SignInButton mSignInButton;
    Button optionalButton;

    public LoginActivity()
    {
        ClientModel.SINGLETON.addObserver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        optionalButton = (Button) findViewById(R.id.button_optional_action);
        optionalButton.setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        boolean existsSavedUser = ClientModel.SINGLETON.getSavedUser(getApplicationContext());

        if(existsSavedUser)
        {
            IPersistence persistence = new FirebasePersistence();
            persistence.updateClientModel();

            Intent intent = TripsActivity.newIntent(getApplicationContext());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.button_optional_action:
//                startNextActivity();
                Intent intent = TripsActivity.newIntent(getApplicationContext());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            acct.getIdToken();
                            FirebaseUser user = mAuth.getCurrentUser();
                            ClientModel.SINGLETON.saveCurrentUser(user, getApplicationContext());

                            IPersistence persistence = new FirebasePersistence();
                            persistence.updateClientModel();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut()
    {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getDisplayName()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.INVISIBLE);
            optionalButton.setText(getString(R.string.welcome_user_fmt, user.getDisplayName()));
            optionalButton.setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);

            optionalButton.setVisibility(View.INVISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(arg.getClass() == ArrayList.class)
        {
            ClientModel.SINGLETON.deleteObserver(this);
            Intent intent = TripsActivity.newIntent(getApplicationContext());
            startActivity(intent);
            finish();
        }
        else if(arg.getClass() == Boolean.class)
        {
            boolean success = (Boolean) arg;
            if(!success)
            {
                updateUI(null);
            }
        }
    }
}
