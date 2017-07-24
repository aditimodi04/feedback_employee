package com.feedback.employee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feedback.employee.dao.Employee;
import com.feedback.employee.utilities.Util;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private EditText mEmailView;
    private EditText mPasswordView;
    private String TAG = "aditi";
    private FirebaseDatabase secondDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Intent intent = new Intent(this, EmployeeList.class);
//            startActivity(intent);
//            finish();
//        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyBg3NfVDph68gvmvyCATux-f3sBVJciIGE")
                .setApplicationId("1:449402630928:android:437412bb9661e18e")
                .setDatabaseUrl("https://feedback-e7e31.firebaseio.com/")
                .build();
        FirebaseApp secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, "Feedback");
        secondDatabase = FirebaseDatabase.getInstance(secondApp);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Util.showProDialog(this);
            DatabaseReference rootRef = secondDatabase.getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Employee employeeData = data.getValue(Employee.class);
                        if (email.equals(employeeData.empEmail) && password.equals(employeeData.empPassword)) {
                            Util.dismissProDialog();
                            Intent intent = new Intent(LoginActivity.this, FeedbackActivity.class);
                            startActivity(intent);
                            return;
                        }

                    }
                    mEmailView.setError("Invalid Email or Password");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Util.showToast(LoginActivity.this, "Something Went wrong");
                    Util.dismissProDialog();
                }
            });
            rootRef.setValue(null);
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.email_sign_in_button:
                attemptLogin();
                break;

        }
    }
}