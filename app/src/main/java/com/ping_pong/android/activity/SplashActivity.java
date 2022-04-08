package com.ping_pong.android.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ping_pong.android.R;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(isInternetConnected()){
            checkUser();
        } else {
            setInternetAlert();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void setInternetAlert() {
        new AlertDialog.Builder(this)
                .setMessage("인터넷과 연결되어 있지 않습니다.")
                .setCancelable(false)
                .setPositiveButton("확인", (dialogInterface, i) -> finishAffinity()).show();
    }

    private void checkUser() {
        if (user != null) {
            Log.d("checkUser", "user : "+ user);
            delayStart(MainActivity.class);
        } else {
            delayStart(GoogleSignUpActivity.class);
        }
    }

    private void delayStart(Class<?> c) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            finish();
            Intent intent = new Intent(SplashActivity.this, c);
            startActivity(intent);
            SplashActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }
}