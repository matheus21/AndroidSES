package app.consultas.com.br.appconsultas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.consultas.com.br.appconsultas.async.AsyncConsultaHttpClient;
import app.consultas.com.br.appconsultas.entity.Consulta;
import app.consultas.com.br.appconsultas.fragment.DatePickerFragment;
import app.consultas.com.br.appconsultas.fragment.TimePickerFragment;
import app.consultas.com.br.appconsultas.repository.ConsultaRepository;
import cz.msebera.android.httpclient.Header;

public class ConsultaActivity extends AppCompatActivity {

    private EditText edtDtconsulta;
    private EditText edtHrconsulta;
    private ConsultaRepository consultaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        edtDtconsulta = (EditText) findViewById(R.id.edtDtconsulta);
        edtHrconsulta = (EditText) findViewById(R.id.edtHrconsulta);
        consultaRepository = new ConsultaRepository(this);

    }

    public void agendarConsulta(View view) {
        Consulta c = montarConsulta();
        boolean validaConsulta = true;

//        consultaRepository.salvarConsulta(c);
//
//        Consulta c1 = consultaRepository.getLastConsulta();
//        Toast.makeText(this, c1.toString(), Toast.LENGTH_LONG).show();

//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//        finish();

        if(c.getData() == null || "".equals(c.getData())) {

            validaConsulta = false;
        }
        if(c.getHora() == null || "".equals(c.getHora())) {

            validaConsulta = false;
        }

        if(validaConsulta) {
            RequestParams params = new RequestParams();
            params.put("paciente_id", c.getPaciente_id());
            params.put("situacao_id", c.getSituacao_id());
            params.put("data", c.getData().getTime());
            params.put("hora", c.getHora());

            AsyncConsultaHttpClient.post("consultas/addConsulta", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String resultado, Throwable throwable) {
                    Log.e(ConsultaActivity.class.getName(), "Erro ao adicionar consulta! Http Code: " + statusCode, throwable);
                    Toast.makeText(ConsultaActivity.this, "Erro ao cadastrar consulta, verifique seu acesso a internet", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String resultado) {
                    Toast.makeText(ConsultaActivity.this, "Consulta agendada com sucesso", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ConsultaActivity.this, ListarConsultasActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Preencha a data E horario da consulta", Toast.LENGTH_SHORT).show();
        }
    }

    public void setData(View view) {

        DatePickerFragment datePickerFragment = new DatePickerFragment();

        Bundle bundle = new Bundle();

        Calendar cal = Calendar.getInstance();

        bundle.putInt("dia", cal.get(Calendar.DAY_OF_MONTH));
        bundle.putInt("mes", cal.get(Calendar.MONTH));
        bundle.putInt("ano", cal.get(Calendar.YEAR));
        datePickerFragment.setArguments(bundle);

        datePickerFragment.setDateListener(dateListener);
        datePickerFragment.show(getFragmentManager(), "Data Consulta");

    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            edtDtconsulta.setText(i2 + "/" + (i1 + 1) + "/" + i);
        }
    };

    public void setHora(View view) {

        TimePickerFragment timePickerFragment = new TimePickerFragment();

        Bundle bundle = new Bundle();

        Calendar cal = Calendar.getInstance();

        bundle.putInt("horas", cal.get(Calendar.HOUR_OF_DAY));
        bundle.putInt("mes", cal.get(Calendar.MINUTE));
        timePickerFragment.setArguments(bundle);

        timePickerFragment.setTimeListener(timeListener);
        timePickerFragment.show(getFragmentManager(), "Hora Consulta.");

    }

    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            edtHrconsulta.setText(i + ":" + i1);
        }
    };

    private Consulta montarConsulta() {

        Consulta consulta = new Consulta();
        SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        int paciente_id = preferences.getInt("paciente_id", 0);
        consulta.setSituacao_id(1);
        consulta.setPaciente_id(paciente_id);
        consulta.setHora(edtHrconsulta.getText().toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dtconsulta = dateFormat.parse(edtDtconsulta.getText().toString());
            consulta.setData(dtconsulta);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return consulta;
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
                Intent i = new Intent(ConsultaActivity.this, LoginActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

