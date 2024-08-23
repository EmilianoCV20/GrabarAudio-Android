package teclag.c85360673.u4grabaraudioapp;


import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import teclag.c20130792.androlib.util.permisos.ChecadorDePermisos;
import teclag.c20130792.androlib.util.permisos.PermisoApp;

public class MainActivity extends AppCompatActivity {


    private PermisoApp [] permisoReq = {
            new PermisoApp ( Manifest.permission.RECORD_AUDIO,
                    "Audio", true ),
            new PermisoApp ( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "Almacenamiento", false ),
            new PermisoApp ( Manifest.permission.READ_MEDIA_AUDIO,
                    "Escuchar", true ),
            new PermisoApp ( Manifest.permission.READ_EXTERNAL_STORAGE,
                    "Almacen2", false ),
    };

    private TextView txtvMensajes;
    private EditText edtGuardarComo;
    private Button   btnGrabar;
    private Button   btnDetener;
    private Button   btnReproducir;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String ruta = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";
    private String fichero;

    private int[] Results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtvMensajes     = findViewById ( R.id.txtvMensaje );
        edtGuardarComo   = findViewById ( R.id.edtGuardarComo );
        btnGrabar        = findViewById ( R.id.btnGrabar );
        btnDetener       = findViewById ( R.id.btnDetener );
        btnReproducir    = findViewById ( R.id.btnReproducir );

        ChecadorDePermisos.checarPermisos ( this, permisoReq );

        txtvMensajes.setText ( "" );
        btnGrabar.setEnabled ( true );
        btnDetener.setEnabled ( false );
        btnReproducir.setEnabled ( false );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ( requestCode == ChecadorDePermisos.CODIGO_PEDIR_PERMISOS ) {
            ChecadorDePermisos.verificarPermisosSolicitados ( this, permisoReq, permissions,
                    grantResults );
            Results = grantResults;
        }
    }

    public void btnGrabarClick (View v ) {
        fichero = ruta + edtGuardarComo.getText().toString() + ".3gp";

        mediaRecorder = new MediaRecorder();
        //Microfono como fuente de  audio
        mediaRecorder.setAudioSource ( MediaRecorder.AudioSource.MIC );
        //Formato Archivo
        mediaRecorder.setOutputFormat ( MediaRecorder.OutputFormat.THREE_GPP );
        //Codificador de Audio
        mediaRecorder.setAudioEncoder ( MediaRecorder.AudioEncoder.AMR_WB );
        //Archivo de salida de la grabacion
        mediaRecorder.setOutputFile ( fichero );

        try {
            txtvMensajes.setText ( "GRABANDO..." );
            btnGrabar.setEnabled ( false );
            btnDetener.setEnabled ( true );
            btnReproducir.setEnabled ( false );

            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch ( IOException ex) {
            txtvMensajes.setText ( "" );
            btnGrabar.setEnabled ( true );
            btnDetener.setEnabled ( false );
            btnReproducir.setEnabled ( false );

            Toast.makeText(this, "FALLO AL CREAR GRABACION", Toast.LENGTH_SHORT).show();
        }

    }

    public void btnDetenerClick ( View v ) {
        txtvMensajes.setText ( "" );
        btnGrabar.setEnabled ( true );
        btnDetener.setEnabled ( false );
        btnReproducir.setEnabled ( true );

        mediaRecorder.stop ();
        mediaRecorder.release ();
    }

    public void btnReproducirClick ( View v ) {
        fichero = ruta + edtGuardarComo.getText().toString() + ".3gp";

        mediaPlayer = new MediaPlayer();
        try {
            //Archivo a reproducir
            mediaPlayer.setDataSource ( fichero );
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    txtvMensajes.setText ( "REPRODUCCIENDO AUDIO..." );
                    btnGrabar.setEnabled ( false );
                    btnDetener.setEnabled ( false );
                    btnReproducir.setEnabled ( false );

                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    txtvMensajes.setText ( "" );
                    btnGrabar.setEnabled ( true );
                    btnDetener.setEnabled ( false );
                    btnReproducir.setEnabled ( true );

                    mediaPlayer.release();
                }
            });
        } catch ( IOException ex ) {
            txtvMensajes.setText ( "" );
            btnGrabar.setEnabled ( true );
            btnDetener.setEnabled ( false );
            btnReproducir.setEnabled ( false );

            Toast.makeText(this, "FALLO LA REPRODUCCION", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnAcercadeClick ( View v ) {
        Intent intent  = new Intent ( this, AcercaDe.class );
        startActivity ( intent );
    }

}
