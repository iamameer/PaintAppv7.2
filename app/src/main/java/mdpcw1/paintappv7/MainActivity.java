package mdpcw1.paintappv7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //global variables
    private ImageView imageView;
    private Button btnNewCanvas,btnLoadPic;
    private ConstraintLayout constraintLayout;
    private TextView textView;
    static final int PICK_IMAGE = 5;

    int newCanvas; //0 - New Canvas // 1 - Load Picture

    Uri uri;

    //initializing items and settings
    public void init(){
        newCanvas = 0;

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(Color.CYAN);

        btnNewCanvas = findViewById(R.id.btnNewCanvas);
        btnLoadPic = findViewById(R.id.btnLoadPic);
        btnLoadPic.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imgFront);

        textView = findViewById(R.id.txtIntro);
        textView.setVisibility(View.VISIBLE);
    }

    //setting up Event Listener
    public void setEvent(){

        //Button - New Canvas ***********************************************
        //https://developer.android.com/reference/android/content/Intent.html
        //https://developer.android.com/training/basics/intents/filters.html
        //"Except as noted, this content is licensed under Creative Commons Attribution 2.5.
        // For details and restrictions, see the Content License."
        btnNewCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //newCanvas = 0;
                Intent intent = new Intent(MainActivity.this,newCanvas.class);
                //intent.putExtra("newCanvas",newCanvas);
                Toast.makeText(getApplicationContext(),"Loading new canvas...",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        //Button - Load Picture ***********************************************
        btnLoadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCanvas = 1;
                Intent intent = new Intent(MainActivity.this,newCanvas.class);
                intent.putExtra("uri",uri.toString());
                intent.putExtra("newCanvas",newCanvas);
                startActivity(intent);
            }
        });

        //Permission - credits to Durai Naidu (classmate) ***********************************************
        //check for permission:
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Request the permission.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_IMAGE);
        }

        //Implicit intent ***********************************************
        //https://google-developer-training.gitbooks.io/android-developer-fundamentals-course-practicals/content/en/Unit%201/23_p_activities_and_implicit_intents.html#task5intro
        //"Except as noted, this content is licensed under Creative Commons Attribution 2.5.
        // For details and restrictions, see the Content License."
        Intent intent = getIntent();
        uri = intent.getData();

        if(Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getType() != null){
            if (intent.getType().startsWith("image/")) {
               imageView.setImageURI(uri);
               btnLoadPic.setVisibility(View.VISIBLE);
               btnNewCanvas.setVisibility(View.INVISIBLE);
               textView.setVisibility(View.INVISIBLE);
            }
        }

    }

    //setting up permission in case some system comes with high security
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE:
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setEvent();
    }
}
