package app.consultas.com.br.appconsultas.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.consultas.com.br.appconsultas.entity.Consulta;
import app.consultas.com.br.appconsultas.util.Constantes;

/**
 * Created by Matheus Oliveira on 02/07/2017.
 */

public class ConsultaRepository  extends SQLiteOpenHelper{


    public ConsultaRepository(Context context) {
        super(context, Constantes.NOME_BD, null, Constantes.VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS CONSULTAS( ");
        query.append(" ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        query.append(" PACIENTE_ID INTEGER(11) NOT NULL, ");
        query.append(" DATA INTEGER NOT NULL, ");
        query.append(" HORA TEXT(20) NOT NULL, ");
        query.append(" SITUACAO INTEGER(1))");

        sqLiteDatabase.execSQL(query.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void salvarConsulta(Consulta consulta) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = getContentValuesConsulta(consulta);

        db.insert("CONSULTAS", null, contentValues);
        db.close();

    }

    @NonNull
    private ContentValues getContentValuesConsulta(Consulta consulta) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PACIENTE_ID", consulta.getPaciente_id());
        contentValues.put("SITUACAO", consulta.getSituacao_id());
        contentValues.put("DATA", consulta.getData().getTime());
        contentValues.put("HORA", consulta.getHora());
        return contentValues;
    }


    public Consulta getLastConsulta() {

        Consulta consulta = new Consulta();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CONSULTAS", null, null, null, null, null, "ID DESC", "1");

        if(cursor.moveToNext()) {
            setConsultaFromCursor(cursor, consulta);
        }

        return consulta;
    }


    private void setConsultaFromCursor(Cursor cursor, Consulta consulta) {
        consulta.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        consulta.setPaciente_id(cursor.getInt(cursor.getColumnIndex("PACIENTE_ID")));
        consulta.setSituacao_id(cursor.getInt(cursor.getColumnIndex("SITUACAO")));
        consulta.setHora(cursor.getString(cursor.getColumnIndex("HORA")));
        long time = cursor.getLong(cursor.getColumnIndex("DATA"));
        Date dtConsulta = new Date();
        dtConsulta.setTime(time);
        consulta.setData(dtConsulta);

    }

}
