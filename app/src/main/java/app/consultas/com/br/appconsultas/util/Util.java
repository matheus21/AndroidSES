package app.consultas.com.br.appconsultas.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import app.consultas.com.br.appconsultas.R;


/**
 * Created by Matheus Oliveira on 25/06/2017.
 */

public class Util {

    /**
     *
     * Confirmação para o deletar
     */

    public static void showMsgConfirm(final Activity activity, String titulo, String txt, TipoMsg tipoMsg, DialogInterface.OnClickListener listener) {
        int theme = 0,  icone = 0;
        switch (tipoMsg) {
            case INFO:
                theme = R.style.MyAlertDialogStyleInfo;
                icone = R.drawable.info;
                break;
            case ERRO:
                theme = R.style.MyAlertDialogStyleErro;
                icone = R.drawable.error;
                break;
            case ALERTA:
                theme = R.style.MyAlertDialogStyleAlert;
                icone = R.drawable.alert;
                break;
            case SUCESSO:
                theme = R.style.MyAlertDialogStyleSuccess;
                icone = R.drawable.success;
                break;
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(activity, theme).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(txt);
        alertDialog.setIcon(icone);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", listener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
            @Override
            /**
             * int i = Qual botão foi clicado -> POSITIVE, NEGATIVE ou NEUTRAL
             */
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public static void showMsgAlertOK(final Activity activity, String titulo, String txt, TipoMsg tipoMsg) {

        int theme = 0,  icone = 0;
        switch (tipoMsg) {
            case INFO:
                theme = R.style.MyAlertDialogStyleInfo;
                icone = R.drawable.info;
                break;
            case ERRO:
                theme = R.style.MyAlertDialogStyleErro;
                icone = R.drawable.error;
                break;
            case ALERTA:
                theme = R.style.MyAlertDialogStyleAlert;
                icone = R.drawable.alert;
                break;
            case SUCESSO:
                theme = R.style.MyAlertDialogStyleSuccess;
                icone = R.drawable.success;
                break;
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(activity, theme).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(txt);
        alertDialog.setIcon(icone);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            /**
             * int i = Qual botão foi clicado -> POSITIVE, NEGATIVE ou NEUTRAL
             */
            public void onClick(DialogInterface dialogInterface, int i) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }


}
