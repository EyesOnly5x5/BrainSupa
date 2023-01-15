package io.github.eyesonly5x5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import de.eyesonly5x5.brainsupa.R;

public class SupaMasterActivity extends AppCompatActivity {
    private int[] btn = { R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9 };
    private int[] layPop = { 0, 1, R.layout.popup_layout_02, R.layout.popup_layout_03, R.layout.popup_layout_04, R.layout.popup_layout_05, R.layout.popup_layout_06, R.layout.popup_layout_07, R.layout.popup_layout_08, R.layout.popup_layout_09, R.layout.popup_layout_10 };
    private int btnLaenge = 2;
    Globals daten = Globals.getInstance();
    int[] BUTTON_IDS;
    private Context myContext = daten.getMyContext();
    TextView vL;

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supahirn);
        BUTTON_IDS = daten.getButtonIDs();
        loadSuperHirn();
        TextView vw = findViewById(R.id.Kopf);
        vw.setTextSize( daten.getMetrics().pxToDp((int)(vw.getTextSize()*2*daten.getMetrics().getFaktor())) );
        vw.setText( daten.getWoMischen() );
        vL = findViewById(R.id.Level);
        vL.setText( "Level:"+(btnLaenge-2) );
        Button Mischa = findViewById(R.id.Mischa);
        Mischa.setTextSize( daten.getMetrics().pxToDp((int)(Mischa.getTextSize()*daten.getMetrics().getFaktor())) );
        Mischa.setOnClickListener(view -> {
            if( Mischa.getText().equals( getApplicationContext().getString( R.string.Mischa ) ) ){
                daten.ColorMischer2( btnLaenge );
                Mischa.setText( R.string.title9a );
                vL.setText( "Level:"+(btnLaenge-2) );
            } else {
                if( checkIt() ) {
                    if( daten.getGewonnen() ){
                        btnLaenge++;
                        if( btnLaenge >= 11 ) btnLaenge = 10;
                        vL.setText( "Level:"+(btnLaenge-2) );
                        saveSuperHirn();
                    }
                    daten.getSoundBib(daten.getGewonnen()).playSound();
                    daten.setGewonnen(true);
                    Mischa.setText(R.string.Mischa);
                } else {
                    daten.incZuege();
                }
            }
        });

        int i = 0;
        for(int id : BUTTON_IDS) {
            Button button = addbtn( id );
            if( i++ > 4 ) button.setVisibility(View.GONE);
            button.setOnClickListener(view -> {
                if( !daten.getGewonnen() ) {
                    int id1 = Integer.parseInt(button.getTag().toString()) - 1;
                    if( daten.colorPos(id1) ) changeColor(id1);
                }
            });
            button.setOnLongClickListener(view -> {
                if( !daten.getGewonnen() ) {
                    int id1 = Integer.parseInt(button.getTag().toString()) - 1;
                    if( daten.colorPos(id1) ) showPopup(view, id1);
                }
                return( true );
            });
            daten.addButton(button);
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_2, menu);
        return( true );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mischy:
                daten.ColorMischer2( btnLaenge );
                return( true );
            case R.id.DeleLevel:
                deleSuperHirn();
                btnLaenge = 2;
                daten.ColorMischer2( btnLaenge );
                vL.setText( "Level:"+(btnLaenge-2) );
                return( true );
            case R.id.AnLeit:
                daten.Anleitung( this, R.string.AnleitSuppa );
                return( true );
        }
        return( super.onOptionsItemSelected( item) );
    }

    private Boolean checkIt(){
        Boolean ret = false;
        int richtig = 0;
        int id1 = 0;
        int id2 = 0;
        int[] werte = daten.checkColor1( );
        for(int i=(daten.getZuege() * 5), j = 0; (i < ((daten.getZuege() + 1) * 5)) && (i < BUTTON_IDS.length); i++, j++ ) {
            id1 = BUTTON_IDS[i];
            Button button = findViewById(id1);
            button.setVisibility(View.VISIBLE);
            if( werte[j] == 1 ){
                id2 = BUTTON_IDS[i-5];
                Button tmpBut = findViewById(id2);
                changeColor( button, tmpBut, id1 );
                tmpBut.setText( "R:"+tmpBut.getText() );
            } else if( werte[j] == 0 ){
                id2 = BUTTON_IDS[i-5];
                Button tmpBut = findViewById(id2);
                tmpBut.setText( "V:"+tmpBut.getText() );
                changeColor( true, id1 );
            } else {
                changeColor( true, id1 );
            }
        }
        for( int j = 0; j < werte.length; j++ ) if( werte[j] == 1 ) richtig++;
        ret = (richtig == 5);
        if( ret ) daten.setGewonnen( true );
        if( daten.getZuege()>=4 ) ret = true;
        return( ret );
    }

    @SuppressLint("ResourceAsColor")
    private Button addbtn( int id) {
        RelativeLayout rLbutty = (RelativeLayout) findViewById(R.id.butty);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(daten.getButx()*daten.getMetrics().getFaktor()), (int)(daten.getButx()*daten.getMetrics().getFaktor()));
        layoutParams.setMargins(10, 10, 0, 0);

        Button button = new Button(this);
        button.setBackgroundColor( button.getContext().getResources().getColor(R.color.black) );
        button.setGravity( Gravity.CENTER );
        if ( id % daten.getAnzahl() == 0 ){
            layoutParams.addRule(RelativeLayout.END_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.RIGHT_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.BELOW, (id-daten.getAnzahl() ) );
            layoutParams.setMargins(10, 10, 20, 0);
            button.setBackgroundColor( button.getContext().getResources().getColor(R.color.DarkGreen) );
        } else if ( id % daten.getAnzahl() == 1 ){
            layoutParams.addRule(RelativeLayout.BELOW, (id-daten.getAnzahl() ) );
            button.setBackgroundColor( button.getContext().getResources().getColor(R.color.DarkGreen) );
        } else {
            layoutParams.addRule(RelativeLayout.END_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.RIGHT_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.BELOW, (id-daten.getAnzahl() ) );
            button.setBackgroundColor( button.getContext().getResources().getColor(R.color.DarkGreen) );
        }
        button.setTag("" + id);
        button.setText("");
        button.setId( id );
        button.setTextSize( daten.getMetrics().pxToDp((int)(getResources().getDimension(R.dimen.SupaHTxt)*daten.getMetrics().getFaktor(true ))) );
        button.setPadding( 0, 0, 0, 0 );

        rLbutty.addView(button, layoutParams);
        return( button );
    }

    // @SuppressLint("ResourceAsColor")
    private void changeColor(int id) {
        changeColor( id, daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] +1 );
    }

    private void changeColor(int id, int color) {
        Button button = daten.buttons.get(id);
        if( color >= btnLaenge ) color = 0;
        daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] = color;
        button.setBackgroundColor( daten.getColor().get( daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] ) );
        button.setText(""+daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()]);
        button.setTextColor( button.getContext().getResources().getColor( R.color.black ) );
        if( daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] == 3 )
            button.setTextColor( button.getContext().getResources().getColor( R.color.white ) );
    }

    private void changeColor( Button btn2, Button btn1, int id ) {
        daten.getColors()[daten.getColors().length-1][(id-1)%daten.getAnzahl()] = Integer.parseInt(btn1.getText().toString());
        btn2.setBackgroundColor( daten.getColor().get( daten.getColors()[daten.getColors().length-1][(id-1)%daten.getAnzahl()] ) );
        btn2.setText( btn1.getText() );
        btn2.setTextColor( btn1.getTextColors() );
    }

    private void changeColor( Boolean Flg, int id ) {
        daten.getColors()[daten.getColors().length-1][(id-1)%daten.getAnzahl()] = -1;
    }

    public void showPopup(View v, int id) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate( layPop[btnLaenge], null);
        PopupWindow popupWindow = new PopupWindow( popupView, (int)(daten.getButy()*daten.getMetrics().getFaktor()*1.5f),(int)((daten.getButy()+popupView.getContext().getResources().getDimension(R.dimen.Space)+3  )*daten.getMetrics().getFaktor()*(1.1f-(btnLaenge*0.01f)))*btnLaenge );

        popupWindow.setOutsideTouchable(true);
        /* popupWindow.setOnDismissListener(() -> {
            // do sth here on dismiss
        });*/
        for( int i = 0; i < btnLaenge; i++ ) {
            TextView tmp = popupView.findViewById(btn[i]);
            tmp.setHeight( (int)(daten.getButy()*daten.getMetrics().getFaktor()) );
            int finalI = i;
            tmp.setOnClickListener(view -> {
                changeColor( id, finalI);
                popupWindow.dismiss();
            });
        }
        popupWindow.showAsDropDown( v );
    }

    private void saveSuperHirn( ){
        String data = "";
        data += ""+btnLaenge+"";
        speichern( "SuperHirn.txt", data );
    }

    private void loadSuperHirn(){
        String data;
        data = laden( "SuperHirn.txt", "2" );
        btnLaenge = Integer.parseInt( data );
    }

    private void deleSuperHirn(){
        loeschen( "SuperHirn.txt" );
    }

    private void speichern( String filename, String data ){
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = myContext.openFileOutput(filename, myContext.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loeschen( String filename ){
        File file = new File( myContext.getFilesDir(), filename );
        file.delete();
    }

    private String laden( String filename, String vorlage ){
        String ret = "";
        ret = vorlage;
        try {
            File in = new File( myContext.getFilesDir(), filename );
            Scanner scanner = new Scanner(in);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                ret = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return( ret );
    }
}
