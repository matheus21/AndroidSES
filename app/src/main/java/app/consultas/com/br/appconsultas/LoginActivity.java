package app.consultas.com.br.appconsultas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;


import app.consultas.com.br.appconsultas.async.AsyncPacienteHttpClient;
import app.consultas.com.br.appconsultas.entity.Paciente;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogar;
    private EditText edtLogin;
    private EditText edtSenha;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogar = (Button) findViewById(R.id.btnLogin);
        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        String login = preferences.getString("login", null);
        String senha = preferences.getString("senha", null);

        if (login != null && senha != null) {
            Intent i = new Intent(LoginActivity.this, DashBoardActivity.class);
            startActivity(i);
            finish();
        }

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validaLogin = true;

                if (edtLogin.getText().toString() == null || "".equals(edtLogin.getText().toString())) {
                    validaLogin = false;
                    edtLogin.setError("Login obrigatorio");
                }

                if (edtSenha.getText().toString() == null || "".equals(edtSenha.getText().toString())) {
                    validaLogin = false;
                    edtSenha.setError("Senha obrigatoria");
                }

                if (validaLogin) {

                    RequestParams params = new RequestParams();
                    params.put("login", edtLogin.getText().toString());
                    params.put("senha", edtSenha.getText().toString());

                    AsyncPacienteHttpClient.post("pacientes/login", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String resultado, Throwable throwable) {
                            Log.e(LoginActivity.class.getName(), "Erro no login do usuario! Http Code: " + statusCode, throwable);
                            Toast.makeText(LoginActivity.this, "Erro ao logar, verifique seu acesso a internet", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String resultado) {
                            Gson pacienteJson = new Gson();
                            Paciente p = pacienteJson.fromJson(resultado, Paciente.class);
                            if (p != null) {
                                if (p.getAcessou() != 2) {
                                    SharedPreferences.Editor editor = LoginActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                                    editor.putString("login", edtLogin.getText().toString());
                                    editor.putString("senha", edtSenha.getText().toString());
                                    editor.putInt("paciente_id", p.getId());
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this, "Bem vindo " + p.getNome() + "!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this, DashBoardActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Caro " + p.getNome() + ", altere sua senha para continuar.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this, AlterarSenhaActivity.class);
                                    i.putExtra("paciente", p);
                                    startActivity(i);
                                    finish();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Usuario ou senha incorretos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
