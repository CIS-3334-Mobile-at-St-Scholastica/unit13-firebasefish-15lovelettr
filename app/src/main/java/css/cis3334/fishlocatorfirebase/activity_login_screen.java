package css.cis3334.fishlocatorfirebase;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_login_screen extends AppCompatActivity {

    private TextView textViewTitle, textView1, textView2;
    private EditText editTextEmail, editTextPass;
    private Button loginBttn, createUserBttn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        loginBttn = (Button) findViewById(R.id.buttonLogin);
        createUserBttn = (Button) findViewById(R.id.buttonCreateUser);

        //Initialize FirebaseAuth instance and the AuthStateListener to track whenever a user signs in and out
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("LoginScreen", "onAuthStateChanged:signed_in:" + user.getUid());
                    signOut();
                    Log.d("LoginScreen", "onAuthStateChanged:signing_user_out:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("LoginScreen", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        loginBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //A Listener for the Login Button, allows a user to "Login" into Firebase
                //Log.d("CIS3334", "normal login "); //Logs in a user normally with Firebase
                signIn(editTextEmail.getText().toString(), editTextPass.getText().toString());
            }
        });

        createUserBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //A listner for the Create User Button, creates a new user account in Firebase
                //Log.d("CIS3334", "Create Account "); //Creates a new user account in Firebase
                createAccount(editTextEmail.getText().toString(), editTextPass.getText().toString());
            }
        });


    }

    /*
    * onStart() - When the activity starts, start the FirebaseAuth listener to listen for logins
    **/
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /*
    * onStop() - When an the activity stops, remove the FirebaseAuth listener from the activity
    **/
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /*
    * createAccount() - takes in an email address and password, validates them, and creates a new user
    * in the Firebase database.
    **/
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LoginScreen", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(activity_login_screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("LoginScreen", "signInWithEmail:onComplete:" + task.isSuccessful());
                            Toast.makeText(activity_login_screen.this, "Sign-in Successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent secActIntent = new Intent(activity_login_screen.this, MainActivity.class);
                            startActivityForResult(secActIntent, 0);
                        }

                        // ...
                    }
                });
    }

    /*
    * signIn() - takes in an email address and password, validates them, and then signs a
    * user into the Firebase database.
    **/
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LoginScreen", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LoginScreen", "signInWithEmail", task.getException());
                            Toast.makeText(activity_login_screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("LoginScreen", "signInWithEmail:onComplete:" + task.isSuccessful());
                            Toast.makeText(activity_login_screen.this, "Sign-in Successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent secActIntent = new Intent(activity_login_screen.this, MainActivity.class);
                            startActivityForResult(secActIntent, 0);
                        }

                        // ...
                    }
                });
    }

    /*
    * signOut() - signs a user out of the Firebase database. Login info is saved though.
    **/
    private void signOut () { mAuth.signOut(); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                signOut();
            }
        }
    }//onActivityResult
}
