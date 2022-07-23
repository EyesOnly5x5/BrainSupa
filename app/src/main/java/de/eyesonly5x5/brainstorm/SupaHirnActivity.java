package de.eyesonly5x5.brainstorm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SupaHirnActivity extends AppCompatActivity {
    private int[] btn = { R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5 };
    Globals daten = Globals.getInstance();
    int[] BUTTON_IDS;

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supahirn);
        // daten.setMyContext( this );
        BUTTON_IDS = daten.getButtonIDs();
        TextView vw = findViewById(R.id.Kopf);
        vw.setTextSize( daten.getMetrics().pxToDp((int)(vw.getTextSize()*2*daten.getMetrics().getFaktor())) );
        Button Mischa = findViewById(R.id.Mischa);
        Mischa.setTextSize( daten.getMetrics().pxToDp((int)(Mischa.getTextSize()*daten.getMetrics().getFaktor())) );
        Mischa.setOnClickListener(view -> {
            daten.ColorMischer();
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
                    /*
                    Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
                    PopupMenu popupMenu = new PopupMenu( wrapper, view );
                    // popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                    for( int i = 0; i < daten.getColor().size(); i++ ) {
                        popupMenu.getMenu().add(1, 500 + i, 1, "slot" + i);
                    }
                    popupMenu.setOnMenuItemClickListener(item -> {
                        if( daten.colorPos(id1) ) changeColor( id1, item.getItemId()-500 );
                        return( true );
                    });
                    popupMenu.show();
                    */
                    if( daten.colorPos(id1) ) showPopup(view, id1);
                }
                return( true );
            });

            daten.addButton(button);
        }

        for( int id = 1; id <= (BUTTON_IDS.length / daten.getAnzahl()); id++ ){
            Button button = addTbtn( id );
            button.setTextColor( R.color.black );
            button.setOnClickListener(view -> {
                if( !daten.getGewonnen()) {
                    int id1 = Integer.parseInt(button.getTag().toString()) - 1;
                    if( daten.colorPos(id1) ) {
                        button.setText("");
                        button.setBackgroundColor(button.getContext().getResources().getColor(R.color.white));
                        daten.decZuege();
                        Button tmp = daten.buttons.get( daten.getMaxFelder()+daten.getZuege()-1 );
                        tmp.setText("<- Setzen");
                        tmp.setBackgroundColor(button.getContext().getResources().getColor(R.color.Gelb));
                        if (daten.checkColor(button)) {
                            daten.setGewonnen(true);
                            tmp = daten.buttons.get(daten.getMaxFelder() );
                            tmp.setText("Bravo");
                            daten.openHiddenColor();
                            daten.getSoundBib(true).playSound();
                        } else {
                            if( daten.getZuege() == 1 ){
                                daten.setGewonnen(true);
                                tmp = daten.buttons.get(daten.getMaxFelder() );
                                tmp.setText("nixDA");
                                daten.openHiddenColor();
                                daten.getSoundBib(false).playSound();
                            }
                        }
                    }
                }
            });
            button.setOnLongClickListener(view -> ( true ));

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
                daten.ColorMischer();
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
        if( id == 1 ) {
            // nixs
            layoutParams.setMargins(10, 10, 0, 20);
        } else if ( id == daten.getAnzahl() ){
            layoutParams.addRule(RelativeLayout.END_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.RIGHT_OF, (id-1) );
            layoutParams.setMargins(10, 10, 20, 20);
        } else if ( id % daten.getAnzahl() == 0 ){
            layoutParams.addRule(RelativeLayout.END_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.RIGHT_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.BELOW, (id-daten.getAnzahl() ) );
            layoutParams.setMargins(10, 10, 20, 0);
            button.setBackgroundColor( button.getContext().getResources().getColor(R.color.DarkGreen) );
        } else if ( id <= daten.getAnzahl() ){
            layoutParams.addRule(RelativeLayout.END_OF, (id-1) );
            layoutParams.addRule(RelativeLayout.RIGHT_OF, (id-1) );
            layoutParams.setMargins(10, 10, 0, 20);
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

    @SuppressLint("ResourceAsColor")
    private Button addTbtn( int id) {
        RelativeLayout rLbutty = (RelativeLayout) findViewById(R.id.butty);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(290, (int)(daten.getButy()*daten.getMetrics().getFaktor()));
        layoutParams.setMargins(10, 10, 0, 0);

        Button button = new Button(this);
        button.setBackgroundColor( button.getContext().getResources().getColor(R.color.gray) );

        button.setText("");
        button.setTransformationMethod(null);
        if( id == 1 ) {
            layoutParams.addRule(RelativeLayout.END_OF, (id*daten.getAnzahl()) );
            layoutParams.addRule(RelativeLayout.RIGHT_OF, (id*daten.getAnzahl()) );
            layoutParams.setMargins(10, 10, 0, 20);
            // button.setText("");
        } else {
            layoutParams.addRule(RelativeLayout.END_OF, (id*daten.getAnzahl()) );
            layoutParams.addRule(RelativeLayout.RIGHT_OF, (id*daten.getAnzahl()) );
            layoutParams.addRule(RelativeLayout.BELOW, ((id*daten.getAnzahl())-daten.getAnzahl()) );
        }
        button.setTag("" + (id+200));
        button.setId( (id+200) );
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
        final View popupView = layoutInflater.inflate(R.layout.popup_layout, null);
        PopupWindow popupWindow = new PopupWindow( popupView, (int)(daten.getButy()*daten.getMetrics().getFaktor()*1.5f), ViewGroup.LayoutParams.WRAP_CONTENT  );

        // popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
            }
        });
        for( int i = 0; i < daten.getColor().size(); i++ ) {
            TextView tmp = popupView.findViewById(btn[i]);
            tmp.setHeight( (int)(daten.getButy()*daten.getMetrics().getFaktor()) );
            tmp.setOnClickListener(view -> {
                // if (daten.colorPos(id)) changeColor(id1);
            });
        }
        popupWindow.showAsDropDown(v);
    }
}
