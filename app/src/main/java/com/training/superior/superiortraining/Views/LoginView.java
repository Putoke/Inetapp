package com.training.superior.superiortraining.Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.training.superior.superiortraining.Controllers.LoginActivity;
import com.training.superior.superiortraining.Models.UserRegisterTask;
import com.training.superior.superiortraining.R;

/**
 * Created by joakim on 15-03-18.
 *
 */
public class LoginView {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public LoginView (final LoginActivity ac) {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) ac.findViewById(R.id.email);

        mPasswordView = (EditText) ac.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    ac.attemptLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        mEmailView.setText("Mugg@EvilDR.se");
        mPasswordView.setText("dasseborg");

        Button mEmailSignInButton = (Button) ac.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ac.attemptLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mLoginFormView = ac.findViewById(R.id.login_form);
        mProgressView = ac.findViewById(R.id.login_progress);

        Button registerButton = (Button) ac.findViewById(R.id.email_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog myDialog = new Dialog(ac);
                myDialog.setContentView(R.layout.register_dialog);
                myDialog.setCancelable(false);
                Button register = (Button) myDialog.findViewById(R.id.register_button);

                myDialog.show();

                register.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String name = ((EditText) myDialog.findViewById(R.id.name)).getText().toString();
                        String lastname = ((EditText) myDialog.findViewById(R.id.lastname)).getText().toString();
                        String email = ((EditText) myDialog.findViewById(R.id.email)).getText().toString();
                        String password = ((EditText) myDialog.findViewById(R.id.password)).getText().toString();
                        UserRegisterTask rTask = new UserRegisterTask(name, lastname, email, password);
                        rTask.execute();
                        myDialog.hide();
                        new AlertDialog.Builder(ac)
                                .setMessage("User registered!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                });

            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show, Activity ac) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = ac.getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setEmailAdapter(ArrayAdapter<String> adapter) {
        mEmailView.setAdapter(adapter);
    }

    public void resetErrors(){
        mEmailView.setError(null);
        mPasswordView.setError(null);
    }

    public void emailError(String error) {
        mEmailView.setError(error);
    }

    public void passwordError(String error) {
        mPasswordView.setError(error);
    }

    public View getPasswordView(){
        return mPasswordView;
    }

    public View getEmailView(){
        return mEmailView;
    }

}
