package mdpcw1.paintappv7;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class selectColor extends AppCompatActivity  {

    //global variable(s)
    private TextView txtColorPreview;
    private Button btnOK, btnCancel;
    private ImageView colorPrev;
    private SeekBar seekRed, seekGreen, seekBlue;
    private Spinner spinnerView;

    private Integer color, oldColor;

    //initialization of items and settings
    private void init() {
        btnOK = findViewById(R.id.btnOK);
        btnCancel = findViewById(R.id.btnCancel);

        colorPrev = findViewById(R.id.colorPrev);

        txtColorPreview = findViewById(R.id.txtColorPreview);
        txtColorPreview.setText("                       ");

        spinnerView = findViewById(R.id.spinnerView);
        ArrayAdapter<CharSequence> staticAdapter =
                ArrayAdapter.createFromResource(this,R.array.colors,R.layout.support_simple_spinner_dropdown_item);
        staticAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerView.setAdapter(staticAdapter);

        Bundle bundle = getIntent().getExtras();
        txtColorPreview.setBackgroundColor(bundle.getInt("setColor"));
        colorPrev.setBackgroundColor(bundle.getInt("setColor"));
        oldColor = bundle.getInt("setColor");
        color = oldColor;

        //seekbar init
        seekBlue = findViewById(R.id.seekBlue);
        seekGreen = findViewById(R.id.seekGreen);
        seekRed = findViewById(R.id.seekRed);
        seekBlue.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        seekGreen.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
        seekRed.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
        seekBlue.setProgress(Color.blue(color));
        seekGreen.setProgress(Color.green(color));
        seekRed.setProgress(Color.red(color));
        seekBlue.setMax(255);seekGreen.setMax(255);seekGreen.setMax(255);
    }

    //setting up Event Listener(s)
    private void setEvent() {
        try{
            //COLOR SPINNER EVENT *****************************************
            spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    switch (i){
                        case 0:
                            //do nothing
                            break;
                        case 1:
                            color = Color.RED;
                            break;
                        case 2:
                            color = Color.CYAN;
                            break;
                        case 3:
                            color = Color.YELLOW;
                            break;
                        case 4:
                            color = Color.GREEN;
                            break;
                        case 5:
                            color = Color.BLUE;
                            break;
                        case 6:
                            color = Color.MAGENTA;
                            break;
                        case 7:
                            color = Color.DKGRAY;
                            break;
                        case 8:
                            color = Color.GRAY;
                            break;
                        case 9:
                            color = Color.WHITE;
                            break;
                        case 10:
                            color = Color.BLACK;
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //Toast for Brush Color - Spinner 1 to 10
                    if (i>0){
                        Toast.makeText(getApplicationContext(),"Brush color: "+String.valueOf(spinnerView.getSelectedItem()),Toast.LENGTH_SHORT).show();
                        colorPrev.setBackgroundColor(color);
                        seekBlue.setProgress(Color.blue(color));
                        seekGreen.setProgress(Color.green(color));
                        seekRed.setProgress(Color.red(color));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    color = oldColor;
                }
            });

            //BUTTON OK EVENT *****************************************
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("newColor",color);
                    bundle.putInt("spinPost",spinnerView.getSelectedItemPosition());
                    Intent result = new Intent();
                    result.putExtras(bundle);
                    setResult(Activity.RESULT_OK,result);
                    finish();
                }
            });

            //BUTTON CANCEL EVENT *****************************************
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            //SEEKBAR WOOHOOO EVENT ***************************************** <3
            //GET RGB value from integer color
            //https://stackoverflow.com/questions/17183587/convert-integer-color-value-to-rgb
             //Jan 23 '15 at 13:57
            //Francesco D.M.
            seekBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    colorPrev.setBackgroundColor(Color.rgb(seekRed.getProgress(),seekGreen.getProgress(),i));
                    color = Color.rgb(seekRed.getProgress(),seekGreen.getProgress(),i);
                    spinnerView.setSelection(0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            seekGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    colorPrev.setBackgroundColor(Color.rgb(seekRed.getProgress(),i,seekBlue.getProgress()));
                    color = Color.rgb(seekRed.getProgress(),i,seekBlue.getProgress());
                    spinnerView.setSelection(0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            seekRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    colorPrev.setBackgroundColor(Color.rgb(i,seekGreen.getProgress(),seekBlue.getProgress()));
                    color = Color.rgb(i,seekGreen.getProgress(),seekBlue.getProgress());
                    spinnerView.setSelection(0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_color);

        init();
        setEvent();
    }

    //killing the activity if back button pressed
    //i wanted to make "press back again to leave" but no time :c
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}