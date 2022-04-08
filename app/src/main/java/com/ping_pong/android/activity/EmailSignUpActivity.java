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
import com.ping_pong.android.databinding.ActivityEmailSignUpBinding;

import java.util.regex.Pattern;

public class EmailSignUpActivity extends AppCompatActivity {
    private ActivityEmailSignUpBinding binding;
    private final int EMAIL_SIGN_UP = 101;
    private FirebaseAuth mAuth;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_sign_up);

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
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.etPasswordCheck.postDelayed(() -> {
                    binding.etPasswordCheck.requestFocus();
                    imm.showSoftInput(binding.etPasswordCheck, 0);
                }, 100);
                return true;
            }
            return false;
        });
        binding.etPasswordCheck.setOnKeyListener((view, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                imm.hideSoftInputFromWindow(binding.etPasswordCheck.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        // 이용 약관 보기
        binding.btnShowTerms1.setText(Html.fromHtml("<u>보기</u>"));
        binding.btnShowTerms1.setOnClickListener(v -> {
            // TODO
        });

        // 개인정보 수집·이용 보기
        binding.btnShowTerms2.setText(Html.fromHtml("<u>보기</u>"));
        binding.btnShowTerms2.setOnClickListener(v -> {
            // TODO
        });

        // 회원가입
        binding.btnEmailSignUp.setOnClickListener(this::emailSignUp);

        // 뒤로가기
        binding.btnBack.setScaleType(ImageView.ScaleType.CENTER);
        binding.btnBack.setColorFilter(ContextCompat.getColor(this, R.color.mainColor));
        binding.btnBack.setOnClickListener(v -> finish());
    }

    // 회원가입
    private void emailSignUp(View view) {
        if (isValidEmail() && isValidPassword() && isValidPasswordCheck() && isTerms1Checked() && isTerms2Checked()) {
            mAuth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            setResult(EMAIL_SIGN_UP);
                            finish();
                        } else {
                            showToast(EmailSignUpActivity.this, "이 이메일로 가입된 계정이 이미 존재합니다");
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

    // 비밀번호 형식 및 null 체크
    private boolean isValidPassword() {
        if (!TextUtils.isEmpty(binding.etPassword.getText())) {
            if (Pattern.matches("^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", binding.etPassword.getText().toString())) {
                binding.tvPasswordWarning.setTextColor(getResources().getColor(R.color.black));
                return true;
            } else {
                showToast(this, "올바른 비밀번호 형식을 입력하세요");
                binding.tvPasswordWarning.setTextColor(getResources().getColor(R.color.red));
                return false;
            }
        } else {
            showToast(this, "비밀번호를 입력하세요");
            return false;
        }
    }

    // 비밀번호 확인 및 null 체크
    private boolean isValidPasswordCheck() {
        if (!TextUtils.isEmpty(binding.etPasswordCheck.getText())) {
            if (binding.etPasswordCheck.getText().toString().equals(binding.etPassword.getText().toString())) {
                return true;
            } else {
                showToast(this, "비밀번호가 일치하지 않습니다");
                return false;
            }
        } else {
            showToast(this, "비밀번호 확인을 입력하세요");
            return false;
        }
    }

    private boolean isTerms1Checked() {
        if (binding.cbTerms1.isChecked()) {
            return true;
        } else {
            showToast(this, "이용약관에 동의해주세요");
            return false;
        }
    }

    private boolean isTerms2Checked() {
        if (binding.cbTerms2.isChecked()) {
            return true;
        } else {
            showToast(this, "개인정보 수집·이용에 동의해주세요");
            return false;
        }
    }

    protected static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}