package com.feedback.employee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.feedback.employee.dao.Employee;
import com.feedback.employee.utilities.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private EditText mEmailView;
    private EditText mPasswordView;
    private String TAG = "aditi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);
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
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("employee");
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Employee employeeData = data.getValue(Employee.class);
                        if (email.equals(employeeData.empEmail) && password.equals(employeeData.empPassword)) {
                            Intent intent = new Intent(LoginActivity.this, FeedbackActivity.class);
                            startActivity(intent);
                            return;
                        }

                    }
                    Util.dismissProDialog();
                    mEmailView.setError("Invalid Email or Password");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Util.showToast(LoginActivity.this, "Something Went wrong");
                    Util.dismissProDialog();
                }
            });
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