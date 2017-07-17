package app.consultas.com.br.appconsultas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import app.consultas.com.br.appconsultas.entity.Consulta;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }

    public void abrirAgendamento(View view) {
        Intent i = new Intent(this, ConsultaActivity.class);
        startActivity(i);
    }

    public void abrirLista(View view) {
        Intent i = new Intent(this, ListarConsultasActivity.class);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logoff, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_sair:
                SharedPreferences.Editor editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
                editor.remove("login");
                editor.remove("senha");
                editor.remove("paciente_id");
                editor.commit();
                finish();
                Intent i = new Intent(DashBoardActivity.this, LoginActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
