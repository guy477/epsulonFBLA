package com.epsulon.fbla_proto_2.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.epsulon.fbla_proto_2.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class loginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_USE_CAMERA = 0;
    private static final int REQUEST_VIEW_MEDIA = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "Guy477:Thomas1515",
            "admin:admin"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    ArrayList<String> serverSimulation;

    private UserLoginTask mAuthTask = null;


    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mPasswordView2;
    private View mProgressView;
    private View mLoginFormView;

    private AlertDialog d;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        serverSimulation = new ArrayList<String>();
        setActivityBackgroundColor(0xffffaa00);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        readLocalUserFiles();

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/FuturaLight.ttf");


        //Remove title bar
        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView2 = (EditText) findViewById(R.id.confirmPassword);

        Button existingAccountButton = (Button) findViewById(R.id.existingAccountButton);
        existingAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: open up dialogue where the user will enter their credentials inorder to login to their account. will be implemented later
                //d.setContentView(R.layout.logincreatdial);
                d = onCreateDialog();
                //d.show();
            }
        });

        Button mEmailCreateButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailCreateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        mLoginFormView = findViewById(R.id.login_progress);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }


    public AlertDialog onCreateDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the input
        final EditText password = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setHint("Password");

        final EditText username = new EditText(this);
        username.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        username.setHint("Username");

        layout.addView(username);
        layout.addView(password);

        layout.setBackgroundColor(0xffbb00);

        builder.setView(layout);

        builder.setMessage("Welcome back")
                .setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it

        final AlertDialog dialog = builder.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                if (isUsernameValid(username.getText().toString()) && isPasswordValid(password.getText().toString())) {
                    wantToCloseDialog = true;
                    attemptLogin(username, password);
                } else if (!isUsernameValid(username.getText().toString())) {
                    username.setError("Invalid username");
                } else {
                    password.setError("Invalid Password");
                }
                if (wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

        return builder.create();
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    //requests permision to view contacts. this enables the app to access and auto complete features.
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    public void readLocalUserFiles() {
        if (serverSimulation.isEmpty()) {
            for (String x : DUMMY_CREDENTIALS) {
                serverSimulation.add(x);
            }
            try {
                FileInputStream inp = openFileInput("newAccount.txt");
                StringBuilder builder = new StringBuilder();
                int ch;
                while ((ch = inp.read()) != -1) {
                    builder.append((char) ch);
                }
                String temp = builder.toString();
                Scanner sc = new Scanner(temp);
                while (sc.hasNextLine()) {
                    serverSimulation.add(sc.nextLine());
                }

            } catch (Exception e) {
                System.out.println("no file exists");
            }

        }
    }

    public void debug() {
        for (String x : serverSimulation) {
            System.out.println(x);
        }
        System.out.println(serverSimulation.size());
    }

    public boolean createAccount() {
        View focus = null;


        //todo: check if the email and passwords are valid. if not, display discrepancies
        if (isUsernameValid(mUsernameView.getText().toString()) && isPasswordValid(mPasswordView.getText().toString()) && !doesAccountExist(mUsernameView.getText().toString())) {
            if (doPasswordMatch()) {
                //todo: validate account creating with server

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                builder.setMessage("Terms And Conditions").setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            //todo write the new user to memory and send a request to store it on the server
                            FileOutputStream s = openFileOutput("newAccount.txt", Context.MODE_PRIVATE);
                            s.write((mUsernameView.getText().toString() + ":" + mPasswordView.getText().toString() + "\n").getBytes());
                            s.close();
                            serverSimulation.add(mUsernameView.getText().toString() + ":" + mPasswordView.getText().toString());
                            debug();
                        } catch (Exception e) {
                            //todo throw exceptions lolol
                            System.out.println("ERROR");
                        }
                    }
                })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mUsernameView.setError("You must agree to the terms and conditions");
                            }
                        });
                AlertDialog a = builder.create();
                a.show();

            } else {
                mPasswordView2.setError("Passwords do not match. Please try again");
                focus = mPasswordView2;
                focus.requestFocus();
            }
        } else if (isUsernameValid(mUsernameView.getText().toString()) && !isPasswordValid(mPasswordView.getText().toString())) {
            mPasswordView.setError("Password must contain at least 4 characters");
            focus = mUsernameView;
            focus.requestFocus();
        } else if (doesAccountExist(mUsernameView.getText().toString())) {
            mUsernameView.setError("Username already exists");
            focus = mUsernameView;
            focus.requestFocus();
        } else if (!isUsernameValid(mUsernameView.getText().toString())) {
            mUsernameView.setError("Username must be 4+ letters long");
            focus = mUsernameView;
            focus.requestFocus();
        } else {
            mUsernameView.setError("You must agree to the terms and conditions");
            focus = mUsernameView;
            focus.requestFocus();
        }
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(EditText username, EditText password) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors. Saves over state changes. required to avoid errors
        username.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String user = username.getText().toString();
        String pass = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user)) {
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        } else if (!isUsernameValid(user)) {
            username.setError(getString(R.string.error_invalid_email));
            focusView = username;
            cancel = true;
        }
        //cancel determines whether to cancel the attempt. Cases incluse invalid password
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            d.show();
            return;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true); //TODO add a seven lakes logo!!!!
            mAuthTask = new UserLoginTask(user, pass);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String email) {
        //TODO: Replace with new logic
        return email.length() >= 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace with new logic
        return password.length() >= 4;
    }

    private boolean doesAccountExist(String username) {
        //TODO: Check to see if the server has the account
        for (String x : serverSimulation) {
            String[] arr = x.split(":");
            if (arr[0].equals(username)) {
                System.out.println("TRUEEEEEEEEEEEEEEEEE");
                return true;
            }
        }
        return false;
    }

    private boolean doPasswordMatch() {
        return mPasswordView.getText().toString().equals(mPasswordView2.getText().toString());
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(loginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service. www.felipeobregon.com

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : serverSimulation) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return (pieces[1].equals(mPassword));
                }
            }

            // TODO: register the new account here.

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(true);

            if (success) {
                try {
                    FileOutputStream s = openFileOutput("username.txt", Context.MODE_PRIVATE);
                    s.write(mEmail.getBytes());
                    s.close();

                } catch (IOException e) {
                    System.out.print("output error 1");
                }
                startActivity(new Intent(loginActivity.this, mainMenuActivity.class));
                showProgress(false);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Your username or password was incorrect. Please try again", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showProgress(false);
                d = onCreateDialog();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
