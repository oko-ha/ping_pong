package com.ping_pong.android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.ping_pong.android.R;
import com.ping_pong.android.databinding.ActivityEmailSignInBinding;

import java.util.regex.Pattern;

public class EmailSignInActivity extends AppCompatActivity {
    private ActivityEmailSignInBinding binding;
    private final int EMAIL_SIGN_IN = 101;
    private FirebaseAuth mAuth;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // EditText 엔터 시 위치 제어
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        binding.etEmail.setOnKeyListener((view, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.etPassword.postDelayed(() -> {
                    binding.etPassword.requestFocus();
                    imm.showSoftInput(binding.etPassword, 0);
                }, 100);
                return true;
            }
            return false;
        });
        binding.etPassword.setOnKeyListener((view, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                imm.hideSoftInputFromWindow(binding.etPassword.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        // 비밀번호 찾기
        binding.btnFindAccount.setText(Html.fromHtml("<u>비밀번호 찾기</u>"));
        binding.btnFindAccount.setOnClickListener(v -> {
            // TODO
        });

        // 로그인
        binding.btnEmailSignIn.setOnClickListener(this::emailSignIn);

        // 뒤로가기
        binding.btnBack.setScaleType(ImageView.ScaleType.CENTER);
        binding.btnBack.setColorFilter(ContextCompat.getColor(this, R.color.mainColor));
        binding.btnBack.setOnClickListener(v -> finish());
    }

    // 로그인
    private void emailSignIn(View view) {
        if (isValidEmail() && isValidPassword()) {
            mAuth.signInWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            setResult(EMAIL_SIGN_IN);

                            finish();
                        } else {
                            showToast(EmailSignInActivity.this, "아이디 또는 비밀번호를 다시 확인해주세요.");
                        }
                    });
        }
    }

    // 이메일 형식 및 null 체크
    private boolean isValidEmail() {
        if (!TextUtils.isEmpty(binding.etEmail.getText())) {
            if (Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", binding.etEmail.getText().toString())) {
                return true;
            } else {
                showToast(this, "올바른 이메일 형식을 입력하세요");
                return false;
            }
        } else {
            showToast(this, "아이디를 입력하세요");
            return false;
        }
    }

    // 비밀번호 null 체크
    private boolean isValidPassword() {
        if (!TextUtils.isEmpty(binding.etPassword.getText())) {
            return true;
        } else {
            showToast(this, "비밀번호를 입력하세요");
            return false;
        }
    }

    protected static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}