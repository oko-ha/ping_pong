package com.ping_pong.android.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ping_pong.android.R;
import com.ping_pong.android.databinding.ActivityGoogleSignUpBinding;

public class GoogleSignUpActivity extends BasicActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private final int EMAIL_SIGN_IN = 101;
    private final int EMAIL_SIGN_UP = 102;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGoogleSignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_google_sign_up);

        mAuth = FirebaseAuth.getInstance();

        // activityResultLauncher 초기화
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            switch (result.getResultCode()) {
                case Activity.RESULT_OK: // 구글 로그인
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        showToast(this, "Google sign in Failed");
                    }
                    break;
                case EMAIL_SIGN_IN: // 이메일 로그인
                    myStartActivity(MainActivity.class);
                    finish();
                case EMAIL_SIGN_UP: // 이메일 가입
                    myStartActivity(IntroActivity.class);
                    finish();
                    break;
            }
        });

        binding.btnGoogleSignUp.setOnClickListener(this::googleSignUp);
        binding.btnEmailSignUp.setOnClickListener(v -> startActivity(new Intent(this, EmailSignUpActivity.class)));
        binding.btnEmailSignIn.setOnClickListener(v -> startActivity(new Intent(this, EmailSignInActivity.class)));
    }

    private void googleSignUp(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        activityResultLauncher.launch(mGoogleSignInClient.getSignInIntent());
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        showToast(GoogleSignUpActivity.this, "Complete");
                        if (mAuth.getCurrentUser() != null)
                            myStartActivity(IntroActivity.class);
                        else
                            showToast(GoogleSignUpActivity.this, "Authentication Failed");
                    } else {
                        showToast(GoogleSignUpActivity.this, "Authentication Failed");
                    }
                });
    }
}