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
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import app.consultas.com.br.appconsultas.async.AsyncPacienteHttpClient;
import app.consultas.com.br.appconsultas.entity.Paciente;
import cz.msebera.android.httpclient.Header;

public class AlterarSenhaActivity extends AppCompatActivity {

    private EditText edtConfirmaSenha;
    private EditText edtNovaSenha;
    private Button btnAlterarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        edtConfirmaSenha = (EditText) findViewById(R.id.edtConfirmaSenha);
        edtNovaSenha = (EditText) findViewById(R.id.edtNovaSenha);
        btnAlterarSenha = (Button) findViewById(R.id.btnAlterarSenha);

        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validaConfirmacao = true;

                if (edtConfirmaSenha.getText().toString() == null || "".equals(edtConfirmaSenha.getText().toString())) {
                    validaConfirmacao = false;
                    edtConfirmaSenha.setError("Digite a confirmação da senha!");
                }

                if (edtNovaSenha.getText().toString() == null || "".equals(edtNovaSenha.getText().toString())) {
                    validaConfirmacao = false;
                    edtNovaSenha.setError("Digite a nova senha!");
                }

                if (!edtNovaSenha.getText().toString().equals(edtConfirmaSenha.getText().toString())) {
                    validaConfirmacao = false;
                    Toast.makeText(AlterarSenhaActivity.this, "As senhas não batem!", Toast.LENGTH_SHORT).show();
                }

                if (validaConfirmacao) {

                    Paciente paciente = (Paciente) getIntent().getExtras().getSerializable("paciente");

                    RequestParams params = new RequestParams();
                    params.put("nova_senha", edtConfirmaSenha.getText().toString());
                    params.put("paciente_id", paciente.getId());

                    AsyncPacienteHttpClient.post("pacientes/alterarSenha", params, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String resultado, Throwable throwable) {
                            Log.e(LoginActivity.class.getName(), "Erro ao alterar senha! Http Code: " + statusCode, throwable);
                            Toast.makeText(AlterarSenhaActivity.this, "Erro ao alterar senha", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String resultado) {

                            Gson pacienteJson = new Gson();
                            Paciente p = pacienteJson.fromJson(resultado, Paciente.class);

                            if (p != null) {
                                SharedPreferences.Editor editor = AlterarSenhaActivity.this.getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                                editor.putString("login", p.getCpf());
                                editor.putString("senha", p.getSenha());
                                editor.putInt("paciente_id", p.getId());
                                editor.commit();
                                Toast.makeText(AlterarSenhaActivity.this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AlterarSenhaActivity.this, DashBoardActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(AlterarSenhaActivity.this, "Ocorreu algum problema...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


    }
}
