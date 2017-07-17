package app.consultas.com.br.appconsultas;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

/**
 * Created by Matheus Oliveira on 02/07/2017.
 */
public class ConsultasApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));
    }
}
