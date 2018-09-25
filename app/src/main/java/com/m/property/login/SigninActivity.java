package com.m.property.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.m.property.MainActivity;
import com.m.property.R;
import com.m.property.beanResponse.Registration;
import com.m.property.beanResponse.userSignin;
import com.m.property.home.HomeActivity;
import com.m.property.utility.AppUtilits;
import com.m.property.utility.Constant;
import com.m.property.utility.DataValidation;
import com.m.property.utility.NetworkUtility;
import com.m.property.utility.SharePreferenceUtils;
import com.m.property.webServices.ServiceWrapper;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {
    private String TAG = "SigninActivity";
    private static final int RC_SIGN_IN = 101;
    private TextView skip, forgot_password, login;
    private EditText phone_no, password;
    private LinearLayout signup_here;

    String first_name, last_name, email, id, type, timestamp,fullname;

    //facebook Login
    LoginButton loginButton;
    CallbackManager callbackManager;

    //Google SignIn
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Log.e(TAG, "  start siggnin Activity");

        skip = (TextView) findViewById(R.id.btn_skip);
        login = (TextView) findViewById(R.id.login);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        signup_here = (LinearLayout) findViewById(R.id.layout_signup_here);

        phone_no = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);

        avi= findViewById(R.id.avi);

        //avi.setIndicator(i);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        //Google signin
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //facebook Integration
        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        // loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setReadPermissions("email", "public_profile");

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userId = loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                displayUserInfo(object);
                            }
                        });

                Bundle bundle = new Bundle();
                bundle.putString("fields", "first_name,last_name,email,id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        signup_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (DataValidation.isValidPhoneNumber(phone_no.getText().toString())) {
                    AppUtilits.displayMessage(SigninActivity.this, getString(R.string.phone_no) + " " + getString(R.string.is_invalid));

                } else if (DataValidation.isNotValidPassword(password.getText().toString())) {
                    AppUtilits.displayMessage(SigninActivity.this, getString(R.string.password) + " " + getString(R.string.is_invalid));

                } else {

                    sendUserLoginData();

                }

            }
        });

    }

    public void sendUserLoginData() {
        //avi.show();
         avi.smoothToShow();

        if (!NetworkUtility.isNetworkConnected(SigninActivity.this)) {
            AppUtilits.displayMessage(SigninActivity.this, getString(R.string.network_not_connected));

        } else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<userSignin> userSigninCall = serviceWrapper.UserSigninCall(phone_no.getText().toString(), password.getText().toString());
            userSigninCall.enqueue(new Callback<userSignin>() {
                @Override
                public void onResponse(Call<userSignin> call, Response<userSignin> response) {

                    Log.d(TAG, "reponse : " + response.toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            // store userdata to share prerference

                            //
                           // avi.hide();
                            avi.smoothToHide();

                            Log.e(TAG, "  user data " + response.body().getInformation());
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_id, response.body().getInformation().getUserId());
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_name, response.body().getInformation().getFullname());
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_email, response.body().getInformation().getEmail());
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_phone, response.body().getInformation().getPhone());
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_DATA, response.body().getInformation().getPhone());



                            // start home activity
                            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                            //intent.putExtra("userid", "sdfsd");
                            startActivity(intent);
                            finish();

                        } else {

                            AppUtilits.displayMessage(SigninActivity.this, response.body().getMsg());
                            // avi.hide();
                            avi.smoothToHide();
                        }
                    } else {

                        AppUtilits.displayMessage(SigninActivity.this, getString(R.string.failed_request));
                        // avi.hide();
                        avi.smoothToHide();

                    }
                }

                @Override
                public void onFailure(Call<userSignin> call, Throwable t) {
                    Log.e(TAG, " failure " + t.toString());
                    // avi.hide();
                    avi.smoothToHide();
                    AppUtilits.displayMessage(SigninActivity.this, getString(R.string.failed_request));

                }
            });


        }


    }

    private void displayUserInfo(JSONObject object) {
        String first_name = "";
        String last_name = "", email = "", id = "";

        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");
            type = "facebook";
            fullname = (""+first_name+" "+last_name);
           // timestamp = "xyz";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharePreferenceUtils.getInstance().saveString(Constant.USER_DATA, id);
        SharePreferenceUtils.getInstance().saveString(Constant.USER_name, first_name);

        //Saving user Data into database
        socialRegister(fullname, email, id, type);

        Toast.makeText(SigninActivity.this, "" + first_name + "" + last_name, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        // super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    //firebase signin
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SigninActivity.this, "sign in sucess", Toast.LENGTH_SHORT).show();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void updateUI() {

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();

            first_name = user.getDisplayName();
            email = user.getEmail();
            id = user.getUid();
            type = "Gmail";
           // timestamp = "gmail_XXXXXXX";

            Toast.makeText(SigninActivity.this, "" + first_name, Toast.LENGTH_SHORT).show();

            //Saving user Data into database
            socialRegister(first_name, email, id, type);
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

          /*  TextView tv_name,tv_email,tv_id;

            tv_name =findViewById(R.id.TV_name);
            tv_email = findViewById(R.id.TV_email);
            tv_id = findViewById(R.id.TV_id);
            tv_name.setText(first_name);
            tv_email.setText("Email id =" +email);
            tv_id.setText("Google id="+id);*/

            //fbLogin( id, email, first_name);

        }
    }


    public void socialRegister(String fullname, String email, String uid, String type) {


        ServiceWrapper serviceWrapper = new ServiceWrapper(null);
        //   Call<PlaceOrderActivity>  call = serviceWrapper.placceOrdercall("1234", SharePreferenceUtils.getInstance().getString(Constant.USER_DATA) )
        Call<Registration> call = serviceWrapper.socialRegister(fullname, email, uid, type);

        call.enqueue(new Callback<Registration>() {
            @Override
            public void onResponse(Call<Registration> call, Response<Registration> response) {
                Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                //  Log.e(TAG, "  ss sixe 1 ");
                if (response.body() != null && response.isSuccessful()) {
                    //    Log.e(TAG, "  ss sixe 2 ");
                    if (response.body().getStatus() == 1) {


                    } else {
                        //AppUtilits.displayMessage(PlaceOrderActivity.this, response.body().getMsg() );
                        Log.e(TAG, "  fail- get user address " + response.body().getMsg());
                    }
                } else {
                    //AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.network_error));
                }
            }

            @Override
            public void onFailure(Call<Registration> call, Throwable t) {

                Log.e(TAG, "  fail- get user address " + t.toString());

            }
        });


    }

}
