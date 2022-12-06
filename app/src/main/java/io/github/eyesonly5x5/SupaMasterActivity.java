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

import de.eyesonly5x5.brainsupa.R;

public class SupaMasterActivity extends AppCompatActivity {
    private int[] btn = { R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9 };
    private int[] layPop = { 0, 1, R.layout.popup_layout_02, R.layout.popup_layout_03, R.layout.popup_layout_04, R.layout.popup_layout_05, R.layout.popup_layout_06, R.layout.popup_layout_07, R.layout.popup_layout_08, R.layout.popup_layout_09, R.layout.popup_layout_10 };
    private int btnLaenge = 2;
    Globals daten = Globals.getInstance();
    int[] BUTTON_IDS;

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supahirn);
        BUTTON_IDS = daten.getButtonIDs();
        TextView vw = findViewById(R.id.Kopf);
        vw.setTextSize( daten.getMetrics().pxToDp((int)(vw.getTextSize()*2*daten.getMetrics().getFaktor())) );
        vw.setText( daten.getWoMischen() );
        Button Mischa = findViewById(R.id.Mischa);
        Mischa.setTextSize( daten.getMetrics().pxToDp((int)(Mischa.getTextSize()*daten.getMetrics().getFaktor())) );
        Mischa.setOnClickListener(view -> {
            daten.ColorMischer2();
        });

        for(int id : BUTTON_IDS) {
            Button button = addbtn( id );
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
        inflater.inflate(R.menu.menu, menu);
        return( true );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mischy:
                daten.ColorMischer2();
                return( true );
            case R.id.AnLeit:
                daten.Anleitung( this, R.string.AnleitSuppa );
                return( true );
        }
        return( super.onOptionsItemSelected( item) );
    }

    @SuppressLint("ResourceAsColor")
    private Button addbtn( int id) {
        RelativeLayout rLbutty = (RelativeLayout) findViewById(R.id.butty);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)(daten.getButy()*daten.getMetrics().getFaktor()), (int)(daten.getButy()*daten.getMetrics().getFaktor()));
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
        Button button = daten.buttons.get(id);
        daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()]++;
        if( daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] >= daten.getColor().size() ) daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] = 0;
        button.setBackgroundColor( daten.getColor().get( daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] ) );
        button.setText(""+daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()]);
        button.setTextColor( button.getContext().getResources().getColor( R.color.black ) );
        if( daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] == 3 )
            button.setTextColor( button.getContext().getResources().getColor( R.color.white ) );
    }

    private void changeColor(int id, int color) {
        Button button = daten.buttons.get(id);
        daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] = color;
        button.setBackgroundColor( daten.getColor().get( daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] ) );
        button.setText(""+daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()]);
        button.setTextColor( button.getContext().getResources().getColor( R.color.black ) );
        if( daten.getColors()[daten.getColors().length-1][id%daten.getAnzahl()] == 3 )
            button.setTextColor( button.getContext().getResources().getColor( R.color.white ) );
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
            // Log.d("Debuggy:", ""+i);
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
}
