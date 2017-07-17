package app.consultas.com.br.appconsultas.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

/**
 * Created by Matheus Oliveira on 27/06/2017.
 * Fragment para utilizar o timepicker
 */

public class TimePickerFragment extends DialogFragment {


    private TimePickerDialog.OnTimeSetListener listener;

    private int horas;
    private int minutos;

    public TimePickerFragment() {

    }

    public void setTimeListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        horas = args.getInt("horas");
        minutos = args.getInt("minutos");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), listener, horas, minutos, true);
    }
}
