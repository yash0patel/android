package com.royal.android25;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.royal.android25.model.ResponseModel;
import com.royal.android25.model.UserModel;
import com.royal.android25.service.SessionService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    TextView tvSignupLink,tvForgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        bind
        edtEmail = findViewById(R.id.edtLoginEmail);
        edtPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLoginSubmit);
        tvSignupLink = findViewById(R.id.tvLoginNewUser);
        tvForgotPasswordLink = findViewById(R.id.tvLoginForgotPassword);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        edtEmail.setText(email);
        edtPassword.setText(password);

        SharedPreferences sp = getSharedPreferences("diamondgame",MODE_PRIVATE);
        String fn = sp.getString("firstName","-1");

        if(!fn.equals("-1")){
            Intent intent1 = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent1);
        }

        tvSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent1);
            }
        });

        tvForgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent1);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                boolean isError = false;

                if (email.isEmpty()) {
                    isError = true;
                    edtEmail.setError("Please Enter Email");
                }

                if (password.isEmpty()) {
                    isError = true;
                    edtPassword.setError("Please Enter Password");
                }

                if (isError) {
                    Toast.makeText(getApplicationContext(), "Please correct error(s)", Toast.LENGTH_SHORT).show();
                } else {
                    Retrofit retrofit =   new Retrofit.Builder()
                            .baseUrl("https://diamondgamenode.onrender.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    SessionService sessionService =  retrofit.create(SessionService.class);

                    UserModel userModel = new UserModel();
                    userModel.setEmail(email);
                    userModel.setPassword(password);

                    Call<ResponseModel> call =  sessionService.loginApi(userModel);

                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.code() == 200 && response.body() != null) {
                                String firstname = response.body().getUser().getFirstName();
                                int credit = response.body().getUser().getCredit();

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                intent.putExtra("firstname", firstname);
//                                intent.putExtra("credit", credit);

                                //sharedpref storage
                                SharedPreferences sp = getSharedPreferences("diamondgame",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("firstName",firstname);
                                editor.putInt("credit",credit);
                                editor.putString("userId",response.body().getUser().get_id());

                                editor.apply();

                                startActivity(intent);
                            } else {
                                // login invalid
                                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                        }
                    });
                }
            }
        });
    }
}