package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class HandwriteViewActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView handwriteViewExit;

    ImageView handwriteViewImage;

    String handwriteId;

    final private static String root = Environment.getExternalStorageDirectory().toString();

    TextView handwriteTitle;
    TextView handwriteSubtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwrite_view);

        handwriteViewExit = (ImageView)findViewById(R.id.handwriteViewExit);
        handwriteViewImage =(ImageView)findViewById(R.id.handwriteViewImage);
        handwriteTitle = (TextView)findViewById(R.id.handwriteTitle);
        handwriteSubtitle = (TextView)findViewById(R.id.handwriteSubtitle);



        Intent intent = getIntent();
        handwriteId = intent.getStringExtra("handwriteId");
        handwriteTitle.setText(intent.getStringExtra("title"));
        handwriteSubtitle.setText(intent.getStringExtra("subtitle"));

        Bitmap bitmap = BitmapFactory.decodeFile(root+"/saved_images/"+handwriteId+".jpg");


        handwriteViewImage.setImageBitmap(bitmap);




        handwriteViewExit.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.handwriteViewExit:
                finish();
                break;
        }
    }
}
