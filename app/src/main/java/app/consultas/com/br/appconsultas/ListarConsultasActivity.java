package app.consultas.com.br.appconsultas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.consultas.com.br.appconsultas.async.AsyncConsultaHttpClient;
import app.consultas.com.br.appconsultas.entity.Consulta;
import app.consultas.com.br.appconsultas.util.TipoMsg;
import app.consultas.com.br.appconsultas.util.Util;
import cz.msebera.android.httpclient.Header;

public class ListarConsultasActivity extends AppCompatActivity {

    private ListView lstConsultas;
    private List<Consulta> listaConsultas;
    private int posicaoSelecionada;
    private ArrayAdapter adapter;
    RelativeLayout lytLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_consultas);
        lytLoading = (RelativeLayout) findViewById(R.id.lytLoading);

        lstConsultas = (ListView) findViewById(R.id.lstConsultas);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        setArrayAdapterConsultas();

        lstConsultas.setOnItemLongClickListener(longClickListener);
        lstConsultas.setOnCreateContextMenuListener(contextMenuListener);

    }

    public void addNovaConsulta(View view) {
        Intent i = new Intent(this, ConsultaActivity.class);
        startActivity(i);
        finish();
    }

    private void setArrayAdapterConsultas() {

        SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        int paciente_id = preferences.getInt("paciente_id", 0);

        RequestParams params = new RequestParams();
        params.put("paciente_id", paciente_id);
        setOnLoading();
        AsyncConsultaHttpClient.post("consultas/listarConsultas", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String resultado, Throwable throwable) {
                Log.e(ConsultaActivity.class.getName(), "Erro ao buscar consultas! Http Code: " + statusCode, throwable);
                Toast.makeText(ListarConsultasActivity.this, "Erro ao buscar consultas, verifique seu acesso a internet", Toast.LENGTH_SHORT).show();
                setOffLoading();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Toast.makeText(ListarConsultasActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                listaConsultas = new ArrayList<Consulta>();
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            listaConsultas.add(new Gson().fromJson(jsonObject.toString(), Consulta.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    List<String> valores = new ArrayList<String>();
                    for (Consulta c : listaConsultas) {
                        valores.add(c.toString());
                    }
                    adapter.clear();
                    adapter.addAll(valores);
                    lstConsultas.setAdapter(adapter);
                    setOffLoading();
                }
            }
        });

    }

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            posicaoSelecionada = i;

            return false;
        }
    };

    private View.OnCreateContextMenuListener contextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            /**
             * Botões do menu dos itens da lista
             */
            contextMenu.add(1, 10, 1, "Solicitar Cancelamento");


        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 10:

                Util.showMsgConfirm(ListarConsultasActivity.this, "Cancelamento", "Deseja realmente solicitar o cancelamento?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int consulta_id = listaConsultas.get(posicaoSelecionada).getId();
                        RequestParams params = new RequestParams();
                        params.put("consulta_id", consulta_id);
                        AsyncConsultaHttpClient.post("consultas/solicitarCancelamento", params, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String resultado, Throwable throwable) {
                                Log.e(ListarConsultasActivity.class.getName(), "Erro ao cancelar consulta! Http Code: " + statusCode, throwable);
                                Toast.makeText(ListarConsultasActivity.this, "Erro ao cancelar consulta, verifique seu acesso a internet", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String resultado) {
                                Toast.makeText(ListarConsultasActivity.this, "Cancelamento solicitado com sucesso!", Toast.LENGTH_SHORT).show();
                                setArrayAdapterConsultas();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });



                break;
            case 20:
                /**
                 * Outro menu...
                 */
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opcoes, menu);
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
                Intent i = new Intent(ListarConsultasActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.atualizar_consultas:
                setArrayAdapterConsultas();
                adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setOffLoading() {
        lytLoading.setVisibility(View.GONE);
    }

    public void setOnLoading() {
        lytLoading.setVisibility(View.VISIBLE);
    }

}
