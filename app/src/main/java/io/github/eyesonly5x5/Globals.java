package io.github.eyesonly5x5;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.eyesonly5x5.brainsupa.R;

public class Globals  extends ListActivity {
    @SuppressLint("StaticFieldLeak")
    private static final Globals instance = new Globals();
    private static MyDisplay metrics;

    // Beispiele für Daten...
    private byte[][] Tast = new byte[100][9];
    private int maxFelder = 0;
    private final boolean[] Flg = new boolean[100];
    private final int[][] Colors = new int[11][5];
    List<Integer> Color = new ArrayList<>();
    int[] BUTTON_IDS;
    private TextView Ausgabe;
    List<Button> buttons = new ArrayList<>();
    List<ImageView> imgButtons = new ArrayList<>();
    List<TextView> TextV = new ArrayList<>();
    private int Zuege = 0;
    private int Anzahl = 0;
    private int Activity=-1;
    private Context myContext;
    private Resources myRes;
    private boolean gewonnen = true;
    private SoundBib SoundW;
    private SoundBib SoundF;
    private int Buty = 90;
    private String woMischen = "Zauber";

    // private Globals() { }

    public static Globals getInstance() {
        return instance;
    }

    public static void setMetrics( Resources hier ){
        metrics = new MyDisplay( hier );
    }
    public static MyDisplay getMetrics( ){
        return( metrics );
    }

    public int getMaxFelder() {
        return this.maxFelder;
    }

    public int getZuege() {
        return Zuege;
    }
    public int decZuege() {
        return --Zuege;
    }
    public int incZuege() {
        return ++Zuege;
    }

    public void setAusgabe(TextView wert) {
        Ausgabe = wert;
    }

    public SoundBib getSoundBib(boolean s) {
        return( (s)?SoundW:SoundF );
    }
    public void setSoundBib(boolean s, SoundBib wert) {
        if( s ) SoundW = wert;
        else SoundF = wert;
    }

    public boolean getGewonnen() {
        return gewonnen;
    }
    public void setGewonnen( boolean wert) {
        gewonnen = wert;
    }

    public void setActivity(int act){
        Activity = act;
    }
    public void setMyContext( MainActivity c) {
        myContext = c;
        myRes = myContext.getResources();
    }

    public void addButton(Button button) {
        buttons.add(button);
    }
    public int getAnzahl(){ return Anzahl; }

    public void setWoMischen( String wert ){
        woMischen = wert;
        metrics.setWoMischen(wert);
    }
    public String getWoMischen( ){ return( woMischen ); }
    public int[][] getColors(){ return( Colors ); }

    public List<Integer> getColor(){ return( Color ); }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void Mischer() {
        int id, id1, id2, tmp;
        Random r = new Random();
        Zuege = 0;
        gewonnen = false;
        Ausgabe.setText("Züge: " + Zuege);
        for (id = 0; id < maxFelder; id++) {
            Button button = buttons.get(id);
            Flg[id] = true;
            button.setBackgroundColor(myRes.getColor(R.color.DarkGreen));
            button.setTextColor(myRes.getColor(R.color.white));
        }
        for (int i = 0; i < 25; i++) {
            id = r.nextInt(maxFelder);
            for (int idS : Tast[id]) if (idS > 0) changeFlg(idS - 1);
        }
    }

    @SuppressLint("SetTextI18n")
    public void ColorMischer() {
        int id;
        Random r = new Random();
        Zuege = (maxFelder / Anzahl);
        gewonnen = false;
        for (int i = 0; i < Anzahl; i++) {
            id = r.nextInt( Color.size() );
            Colors[0][i] = id;
            Colors[Colors.length-1][i] = -1;
        }
        for (id = 0; id < maxFelder; id++) {
            Button button = buttons.get(id);
            if( id < Anzahl ) {
                button.setBackgroundColor(myRes.getColor(R.color.black));
            } else {
                button.setBackgroundColor(myRes.getColor(R.color.DarkGreen));
            }
            button.setText("");
        }
        for (id = 0; id < Zuege; id++) {
            Button button = buttons.get(maxFelder+id);
            button.setText( (id!=(Zuege-1))?"":"<- Setzen" );
            button.setBackgroundColor( myRes.getColor( (id!=(Zuege-1))?R.color.gray: R.color.Gelb ) );
        }
    }

    public void ColorMischer2(int btnLaenge) {
        int id;
        Random r = new Random();
        Zuege = 1;
        gewonnen = false;
        for (int i = 0; i < Anzahl; i++) {
            id = r.nextInt( btnLaenge );
            Colors[0][i] = id;
            Colors[Colors.length-1][i] = -1;
        }
        for (id = 0; id < maxFelder; id++) {
            Button button = buttons.get(id);
            button.setBackgroundColor(myRes.getColor(R.color.DarkGreen));
            button.setText("");
        }
    }

    public void openHiddenColor(){
        for (int id = 0; id < Anzahl; id++) {
            Button button = buttons.get(id);
            button.setBackgroundColor( Color.get( Colors[0][id] ) );
        }
    }

    @SuppressLint("WrongConstant")
    private int anzahlButtons(){
        int AnzBut;
        if( "|SupaMaster|".contains(getWoMischen()) ) {
            AnzBut = 4;
        } else {
            AnzBut = (((metrics.getMaxPixels()) / (int) (this.Buty * metrics.getFaktor())) - 3);
        }
        if( AnzBut > 11 ) AnzBut = 11;
        AnzBut *= Anzahl;
        return( AnzBut );
    }

    public int getButy(){ return( (metrics.getFaktor()<2)?Buty:Buty*3/4 ); }

    public int[] getButtonIDs() {
        int wer = getWerWoWas();
        int Buttys = (wer==0)?9:(wer==1)?16:(wer==2)?25:(wer==3)?anzahlButtons():(wer==4)?100:(wer>=5)?Anzahl*Anzahl:0;
        int[] ret = new int[Buttys];

        if( wer < 3 ){
            for (int i = 0; i < ret.length; i++) {
                ret[i] = myRes.getIdentifier("b"+(i+1), "id", myContext.getPackageName());
            }
        } else {
            for (int i = 0; i < ret.length; i++) {
                ret[i] = (i + 1);
            }
        }
        BUTTON_IDS = ret;
        maxFelder = BUTTON_IDS.length;
        return (BUTTON_IDS);
    }

    @SuppressLint("NonConstantResourceId")
    private int getWerWoWas(){
        int ret = -1;
        switch( Activity ){
            case R.layout.activity_supahirn:
                ret = 3;
                break;
        }
        return( ret );
    }

    public void setGameData(int[] wert, int anzahl) {
        Zuege = 0;
        gewonnen = true;
        buttons = null;
        buttons = new ArrayList<>();
        imgButtons = null;
        imgButtons = new ArrayList<>();
        TextV = null;
        TextV = new ArrayList<>();
        Color = null;
        Color = new ArrayList<>();
        Anzahl = anzahl;

        for (int x : wert) Color.add(x);
    }

    @SuppressLint("ResourceAsColor")
    public void changeFlg(int id) {
        Button button = buttons.get(id);
        if (Flg[id]) {
            button.setBackgroundColor(myRes.getColor(R.color.DarkRed));
            button.setTextColor(myRes.getColor(R.color.Gelb));
        } else {
            button.setBackgroundColor(myRes.getColor(R.color.DarkGreen));
            button.setTextColor(myRes.getColor(R.color.white));
        }
        Flg[id] = !Flg[id];
    }

    public boolean checkColor( Button button ){
        int richtig=0, vorhanden=0;
        int[][] c = new int[2][Anzahl];
        for( int i=0; i < Anzahl; i++ ) {
            c[0][i] = Colors[0][i];
            c[1][i] = Colors[Colors.length - 1][i];
            Colors[Colors.length - 1][i] = -1;
            if (c[0][i] == c[1][i]) {
                richtig++;
                c[0][i] = -1;
                c[1][i] = -1;
            }
        }
        for( int i=0; i < Anzahl; i++ ) {
            for( int j=0; j < Anzahl; j++ ){
                if( (c[0][j] != -1) && (c[1][i] != -1) && (c[0][j] == c[1][i]) ){
                    vorhanden++;
                    c[0][j] = -1;
                    c[1][i] = -1;
                }
            }
        }
        button.setText("R:"+richtig+" / V:"+vorhanden);
        return( richtig == Anzahl );
    }

    public int[] checkColor1( ){
        int[] ret = new int[5];
        int[][] c = new int[2][Anzahl];
        for( int i=0; i < Anzahl; i++ ) {
            ret[i] = -1;
            c[0][i] = Colors[0][i];
            c[1][i] = Colors[Colors.length - 1][i];
            Log.d("Debuggy:","I:"+i+" c0:"+c[0][i]+" c1:"+c[1][i]);
            Colors[Colors.length - 1][i] = -1;
            if (c[0][i] == c[1][i]) {
                ret[i] = 1;
                c[0][i] = -1;
                c[1][i] = -1;
            }
        }
        for( int i=0; i < Anzahl; i++ ) {
            for( int j=0; j < Anzahl; j++ ){
                if( (c[0][j] != -1) && (c[1][i] != -1) && (c[0][j] == c[1][i]) ){
                    ret[i] = 0;
                    c[0][j] = -1;
                    c[1][i] = -1;
                }
            }
        }
        return( ret );
    }

    public boolean checkColor2( ){
        int richtig=0, vorhanden=0;
        int[][] c = new int[2][Anzahl];
        for( int i=0; i < Anzahl; i++ ) {
            c[0][i] = Colors[0][i];
            c[1][i] = Colors[Colors.length - 1][i];
            Log.d("Debuggy:","I:"+i+" c0:"+c[0][i]+" c1:"+c[1][i]);
            Colors[Colors.length - 1][i] = -1;
            if (c[0][i] == c[1][i]) {
                richtig++;
                c[0][i] = -1;
                c[1][i] = -1;
            }
        }
        for( int i=0; i < Anzahl; i++ ) {
            for( int j=0; j < Anzahl; j++ ){
                if( (c[0][j] != -1) && (c[1][i] != -1) && (c[0][j] == c[1][i]) ){
                    vorhanden++;
                    c[0][j] = -1;
                    c[1][i] = -1;
                }
            }
        }
        // button.setText("R:"+richtig+" / V:"+vorhanden);
        return( richtig == Anzahl );
    }

    public boolean colorPos( int id ){
        boolean Flage;
        Flage = ((Zuege+200) == (id+1)) || (id >= (((Zuege*Anzahl)-Anzahl)) && (id < (Zuege*Anzahl)));
        // Log.d("Debuggy:", "Züege:"+Zuege+" id:"+id+" Anzahl:"+Anzahl );
        return( Flage );
    }

    static class SoundBib extends AppCompatActivity {
        private SoundPool soundPool;
        List<Integer> sound = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // setContentView(R.layout.activity_main);
        }

        public SoundBib(boolean s, Context context) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();

            if( s ) {
                sound.add(soundPool.load(context, R.raw.won1, 1));
                sound.add(soundPool.load(context, R.raw.won2, 1));
                sound.add(soundPool.load(context, R.raw.won3, 1));
                sound.add(soundPool.load(context, R.raw.won4, 1));
                sound.add(soundPool.load(context, R.raw.won5, 1));
            } else {
                sound.add(soundPool.load(context, R.raw.fail1, 1));
                sound.add(soundPool.load(context, R.raw.fail2, 1));
                sound.add(soundPool.load(context, R.raw.fail3, 1));
                sound.add(soundPool.load(context, R.raw.fail4, 1));
            }
        }

        // When users click on the button "Gun"
        public void playSound() {
            soundPool.autoPause();
            Random r = new Random();
            int id = r.nextInt(sound.size());
            soundPool.play(sound.get(id), 0.25F, 0.25F, 0, 0, 1);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            soundPool.release();
            soundPool = null;
        }
    }

    public void Anleitung( Context dasDA, int Wat ) {
        Dialog customDialog = new Dialog( dasDA );
        customDialog.setContentView(R.layout.anleitung);
        TextView oView = customDialog.findViewById( R.id.Anleitung );
        String str = myRes.getString( Wat, myRes.getString( R.string.Wunschliste ) );
        oView.setText( str );
        Button bView = customDialog.findViewById( R.id.Warte );
        bView.setOnClickListener(view -> customDialog.dismiss());
        customDialog.setCancelable(false);
        customDialog.show();
    }
}