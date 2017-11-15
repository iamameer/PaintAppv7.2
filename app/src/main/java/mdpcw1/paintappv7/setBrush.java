package mdpcw1.paintappv7;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class setBrush extends AppCompatActivity {

    //global variable(s)
    private Button btnBOK,btnBCancel;
    private ImageButton btnBRound,btnBRect,btnBButt;
    private SeekBar seekBarSize;
    private EditText txtMultiplier,txtSize;

    private int brushType;

    //initialization of items and settings
    private void init() {
        btnBOK = findViewById(R.id.btnBOK);
        btnBCancel = findViewById(R.id.btnBCancel);

        btnBRound = findViewById(R.id.btnBRound);
        btnBRect = findViewById(R.id.btnBRect);
        btnBButt = findViewById(R.id.btnBButt);

        txtMultiplier = findViewById(R.id.txtMultiplier);
        txtSize = findViewById(R.id.txtSize);

        Bundle bundle = getIntent().getExtras();
        txtSize.setText(Integer.toString(bundle.getInt("brushSize")));
        txtMultiplier.setText("1");

        seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSize.setProgress(bundle.getInt("brushSize"));
        brushType = bundle.getInt("brushType");

        switch (bundle.getInt("brushType")){
            case 1:
                btnBRound.setBackgroundColor(Color.CYAN);
                btnBRect.setBackgroundColor(Color.TRANSPARENT);
                btnBButt.setBackgroundColor(Color.TRANSPARENT);
                break;
            case 2:
                btnBRound.setBackgroundColor(Color.TRANSPARENT);
                btnBRect.setBackgroundColor(Color.CYAN);
                btnBButt.setBackgroundColor(Color.TRANSPARENT);
                break;
            case 3:
                btnBRound.setBackgroundColor(Color.TRANSPARENT);
                btnBRect.setBackgroundColor(Color.TRANSPARENT);
                btnBButt.setBackgroundColor(Color.CYAN);
                break;
            default:
                Toast.makeText(getApplicationContext(),"Error loading brush type",Toast.LENGTH_LONG).show();
                break;
        }
    }

    //setting up Event Listener(s)
    private void setEvent() {
        try{
            //BRUSH ROUND SHAPE EVENT *****************************************
            btnBRound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    brushType = 1;
                    btnBRound.setBackgroundColor(Color.CYAN);
                    btnBRect.setBackgroundColor(Color.TRANSPARENT);
                    btnBButt.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getApplicationContext(),"Brush type: ROUND",Toast.LENGTH_SHORT).show();
                }
            });

            //BRUSH RECT SHAPE EVENT *****************************************
            btnBRect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    brushType = 2;
                    btnBRound.setBackgroundColor(Color.TRANSPARENT);
                    btnBRect.setBackgroundColor(Color.CYAN);
                    btnBButt.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getApplicationContext(),"Brush type: SQUARE",Toast.LENGTH_SHORT).show();
                }
            });

            //BRUSH BUTT SHAPE EVENT *****************************************
            btnBButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    brushType = 3;
                    btnBRound.setBackgroundColor(Color.TRANSPARENT);
                    btnBRect.setBackgroundColor(Color.TRANSPARENT);
                    btnBButt.setBackgroundColor(Color.CYAN);
                    Toast.makeText(getApplicationContext(),"Brush type: BUTT",Toast.LENGTH_SHORT).show();
                }
            });

            //SEEKBAR EVENT *****************************************
            seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i,boolean b) {
                    if (txtMultiplier.getText()!=null) {
                        try {
                            txtSize.setText(String.valueOf(Integer.valueOf(txtMultiplier.getText().toString()) * i));
                        } catch (NumberFormatException e) {
                            txtMultiplier.setText("1");
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (txtMultiplier.getText()==null){
                        txtMultiplier.setText("1");
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            //EDITTEXT CHANGE EVENT *****************************************
            //this is to set Multiplier limit
            //apparently on hold cuz something's wrong here regarding textchange() event
            //value goes null easily


            //BUTTON OK EVENT *****************************************
            btnBOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    if (txtSize.getText()==null){txtSize.setText("0");}
                    bundle.putInt("brushSize",Integer.valueOf(txtSize.getText().toString()));
                    bundle.putInt("brushType",brushType);
                    Intent result = new Intent();
                    result.putExtras(bundle);
                    setResult(Activity.RESULT_OK,result);
                    finish();
                }
            });

            //BUTTON CANCEL EVENT *****************************************
            btnBCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_brush);

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
