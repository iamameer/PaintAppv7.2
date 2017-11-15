package mdpcw1.paintappv7;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class newCanvas extends AppCompatActivity {

    //global variable(s)
    static final int CODE_COLOR = 10,CODE_BRUSH=20,CODE_LOAD=30;
    private ConstraintLayout constraintLayout;
    private FrameLayout frameLayout;
    private SeekBar seekBar;
    private Spinner spinner;
    private ImageButton btnRound,btnRect;
    public ImageButton btnNLoadPic;
    private TextView txtSize;

    //0 - New Canvas // 1 - Load Picture
    //brushtype: 1 - Round, 2 - Rectangle, 3 - Butt ( ͡° ͜ʖ ͡°)
    int newCanvas,brushType;

    Uri uri, newUri;
    FingerPainterView fpv;
    AlertDialog.Builder builder;

    //initialization of items and settings
    private void init() throws FileNotFoundException {
        newCanvas = 0;
        brushType = 1;

        frameLayout = findViewById(R.id.frmCanvas);
        fpv = findViewById(R.id.fpv);
        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(Color.DKGRAY);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(fpv.getBrushWidth());
        seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(fpv.getColour(),PorterDuff.Mode.SRC_IN));

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> staticAdapter =
                ArrayAdapter.createFromResource(this,R.array.colors,R.layout.support_simple_spinner_dropdown_item);
        staticAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(staticAdapter);

        txtSize = findViewById(R.id.txtSize);
        txtSize.setTextColor(Color.WHITE);
        txtSize.setText(" Size: "+Integer.toString(fpv.getBrushWidth()));

        btnRound = findViewById(R.id.btnRound);
        btnRect = findViewById(R.id.btnRect);
        btnNLoadPic = findViewById(R.id.btnNLoadPic);

        btnRound.setBackgroundColor(Color.CYAN);
        btnRect.setBackgroundColor(Color.TRANSPARENT);
        btnNLoadPic.setBackgroundColor(Color.TRANSPARENT);


        newCanvas = getIntent().getIntExtra("newCanvas",0);
        if (newCanvas ==1){
            uri = Uri.parse(getIntent().getStringExtra("uri"));
            fpv.load(uri);
            loadImage(uri);
        }
    }

    //setting up Event Listener(s)
    private void setEvent(){

        try {
            //Seek Bar Referenced from:
            //https://examples.javacodegeeks.com/android/core/widget/seekbar/android-seekbar-example/
            //Thodoris Bais - Android SeekBar Example (July 2014)
            //All trademarks and registered trademarks appearing on Java Code Geeks are the property of their respective owners.
            // Java is a trademark or registered trademark of Oracle Corporation in the United States and other countries.
            // Examples Java Code Geeks is not connected to Oracle Corporation and is not sponsored by Oracle Corporation.
            //SEEKBAR EVENT *****************************************
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    fpv.setBrushWidth(seekBar.getProgress());
                    txtSize.setText(" Size: "+Integer.toString(seekBar.getProgress()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    fpv.setBrushWidth(seekBar.getProgress());
                }
            });

            //TEXTVIEW EVENT *****************************************
            txtSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //start setBrush activity
                    Intent intent = new Intent(newCanvas.this,setBrush.class);
                    startActivity(intent);
                }
            });

            //COLOR SPINNER EVENT *****************************************
            //Drop down list referenced from:
            //http://www.ahotbrew.com/android-dropdown-spinner-example/
            //ANDROID DROP DOWN LIST EXAMPLE
            // gurisingh April 15, 2015
            //EVENT 1 = SINGLE CLICK
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    switch (i){
                        case 0:
                            //do nothing
                            break;
                        case 1:
                            fpv.setColour(Color.RED);
                            break;
                        case 2:
                            fpv.setColour(Color.CYAN);
                            break;
                        case 3:
                            fpv.setColour(Color.YELLOW);
                            break;
                        case 4:
                            fpv.setColour(Color.GREEN);
                            break;
                        case 5:
                            fpv.setColour(Color.BLUE);
                            break;
                        case 6:
                            fpv.setColour(Color.MAGENTA);
                            break;
                        case 7:
                            fpv.setColour(Color.DKGRAY);
                            break;
                        case 8:
                            fpv.setColour(Color.GRAY);
                            break;
                        case 9:
                            fpv.setColour(Color.WHITE);
                            break;
                        case 10:
                            fpv.setColour(Color.BLACK);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //Toast for Brush Color - Spinner 1 to 10
                    if (i>0){Toast.makeText(getApplicationContext(),"Brush color: "+String.valueOf(spinner.getSelectedItem()),Toast.LENGTH_SHORT).show();}
                    seekBar.setProgress(fpv.getBrushWidth());
                    seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(fpv.getColour(),PorterDuff.Mode.SRC_IN));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //do nothing
                }
            });

            //EVENT 2 = LONG CLICK
            //Bundle referenced from:
            //https://stackoverflow.com/questions/17878907/get-imageviews-image-and-send-it-to-an-activity-with-intent
            //Bald bcs of IT
            //July 2013
            spinner.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //start selectColor activity
                    Intent intent = new Intent(newCanvas.this,selectColor.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("setColor",fpv.getColour());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,CODE_COLOR);
                    return false;
                }
            });

            //BRUSH SHAPE EVENT *****************************************
            //EVENT 1 - SINGLE CLICK
            btnRound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fpv.setBrush(Paint.Cap.ROUND);
                    brushType = 1;
                    btnRound.setBackgroundColor(Color.CYAN);
                    btnRect.setBackgroundColor(Color.TRANSPARENT);
                    Toast.makeText(getApplicationContext(),"Brush type: "+fpv.getBrush().toString(),Toast.LENGTH_SHORT).show();
                }
            });
            btnRect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fpv.setBrush(Paint.Cap.SQUARE);
                    brushType = 2;
                    btnRound.setBackgroundColor(Color.TRANSPARENT);
                    btnRect.setBackgroundColor(Color.CYAN);
                    Toast.makeText(getApplicationContext(),"Brush type: "+fpv.getBrush().toString(),Toast.LENGTH_SHORT).show();
                }
            });

            //EVENT 2 - LONG CLICK
            btnRound.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(newCanvas.this,setBrush.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("brushSize",fpv.getBrushWidth());
                    if(fpv.getBrush()== Paint.Cap.BUTT){brushType=3;}
                    bundle.putInt("brushType",brushType);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,CODE_BRUSH);
                    return false;
                }
            });
            btnRect.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(newCanvas.this,setBrush.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("brushSize",fpv.getBrushWidth());
                    if(fpv.getBrush()== Paint.Cap.BUTT){brushType=3;}
                    bundle.putInt("brushType",brushType);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,CODE_BRUSH);
                    return false;
                }
            });

            //LOAD BUTTON EVENT *****************************************
            btnNLoadPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent loadPic = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    //location
                    File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    String picDirPath = picDir.getPath();
                    Uri data = Uri.parse(picDirPath);
                    //set data and type
                    loadPic.setDataAndType(data,"image/*");
                    startActivityForResult(loadPic,CODE_LOAD);

                    Toast.makeText(getApplicationContext(),"Select 1(one) NEW image from the gallery",Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    //method to read Image file as drawable
    public void loadImage(Uri newUriL){
        newCanvas = 0;
        try{
            fpv.load(newUriL);
            InputStream inputStream = getContentResolver().openInputStream(newUriL);
            Drawable drawable = Drawable.createFromStream(inputStream, newUriL.toString() );
            //TO-IMPROVE: rescale the bitmap/canvas :c
            //atm its not working
            //Bitmap bit = ((BitmapDrawable)drawable).getBitmap();
            //Bitmap bit2 = Bitmap.createScaledBitmap(bit,Math.max(fpv.getWidth(), fpv.getHeight()), Math.max(fpv.getWidth(), fpv.getHeight()),false);
            //Drawable drawable2 = new BitmapDrawable(getResources(),bit2);
            fpv.setBackground(drawable);
        }catch (IOException e){
            Toast.makeText(getApplicationContext(),"Error loading picture",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_canvas);

        try{
            init();
        }catch (IOException e){
            Toast.makeText(getApplicationContext(),"Error loading picture",Toast.LENGTH_SHORT).show();
        }
        setEvent();

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(resultCode,resultCode,data);

        switch (requestCode){
            case CODE_COLOR: //intentResult of selectColor Activity
                if (resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    fpv.setColour(bundle.getInt("newColor"));
                    spinner.setSelection(bundle.getInt("spinPost"));
                    seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(fpv.getColour(),PorterDuff.Mode.SRC_IN));
                }
                break;
            case CODE_BRUSH: //intentResult of setBrush Activity
                if (resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    fpv.setBrushWidth(bundle.getInt("brushSize"));
                    txtSize.setText(String.valueOf(fpv.getBrushWidth()));
                    switch (bundle.getInt("brushType")){
                        case 1:
                            btnRound.performClick();
                            break;
                        case 2:
                            btnRect.performClick();
                            break;
                        case 3:
                            fpv.setBrush(Paint.Cap.BUTT);
                            btnRound.setBackgroundColor(Color.TRANSPARENT);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),"Error setting brush type",Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                break;
            case CODE_LOAD: //intentResult of loadNewPicture Activity
                if (resultCode==RESULT_OK){
                    newUri = data.getData();
                    //uri = newUri;
                    //Alert dialog
                    //https://developer.android.com/guide/topics/ui/dialogs.html
                    //Except as noted, this content is licensed under Creative Commons Attribution 2.5. For details and
                    // restrictions, see the Content License.
                    //David Hedlund May 18 at 10:37
                    //https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Load new Image")
                            .setMessage("Clear drawing as well?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { //ok
                                    fpv.clearCanvas();
                                    fpv.load(newUri);
                                    loadImage(newUri);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { //cancel
                                    fpv.load(newUri);
                                    loadImage(newUri);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            default: //other activity: do nothing
                break;
        }
    }


    //My attempt at handling screen orientation change :c
    //not fully working
    //original intention was to create multiple XML files
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //supposedly fetching new uri from line 355
       /* if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && uri != null){
            fpv.load(uri);
            loadImage(uri);
        } */
        setContentView(R.layout.activity_main);
    }

    //killing the activity if back button pressed
    //i wanted to make "press back again to leave" but no time :c
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
