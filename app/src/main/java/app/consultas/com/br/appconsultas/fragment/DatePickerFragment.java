package app.consultas.com.br.appconsultas.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Matheus Oliveira on 27/06/2017.
 * Fragment para utilizar o datepicker
 */

public class DatePickerFragment extends DialogFragment {


    private DatePickerDialog.OnDateSetListener listener;

    private int dia;
    private int mes;
    private int ano;

    public DatePickerFragment() {

    }

    public void setDateListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        dia = args.getInt("dia");
        mes = args.getInt("mes");
        ano = args.getInt("ano");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), listener, ano, mes, dia);
    }
}
