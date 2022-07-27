package de.eyesonly5x5.brainsupa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Globals daten = Globals.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(  R.layout.activity_main);
        daten.setMyContext( this );
        daten.setMetrics(getResources());

        TextView AusG = findViewById(R.id.Kopf);
        AusG.setText(getString(R.string.title1));
        AusG.setTextSize( daten.getMetrics().pxToDp((int)(AusG.getTextSize()*daten.getMetrics().getFaktor())) );
        daten.setSoundBib(true,new Globals.SoundBib( true,this));
        daten.setSoundBib(false,new Globals.SoundBib( false,this));

        Button SupaHirn = findViewById(R.id.SupaHirn);
        SupaHirn.setTextSize( daten.getMetrics().pxToDp((int)(SupaHirn.getTextSize()*daten.getMetrics().getFaktor())) );
        Button SupraHirni = findViewById(R.id.SupraHirni);
        SupraHirni.setTextSize( daten.getMetrics().pxToDp((int)(SupraHirni.getTextSize()*daten.getMetrics().getFaktor())) );

        SupaHirn.setOnClickListener(view -> {
            SupaHirn.setBackgroundColor(getResources().getColor(R.color.DarkRed));
            daten.setActivity(R.layout.activity_supahirn);
            daten.setWoMischen( "SupaHirn" );
            daten.setGameData(getResources().getIntArray(R.array.color6),4);
            startActivity(new Intent(getApplicationContext(),SupaHirnActivity.class));
            SupaHirn.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
        });
        SupraHirni.setOnClickListener(view -> {
            SupraHirni.setBackgroundColor(getResources().getColor(R.color.DarkRed));
            daten.setActivity(R.layout.activity_supahirn);
            daten.setWoMischen( "SupraHirni" );
            daten.setGameData(getResources().getIntArray(R.array.color8),5);
            startActivity(new Intent(getApplicationContext(),SupaHirnActivity.class));
            SupraHirni.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
        });
    }
}