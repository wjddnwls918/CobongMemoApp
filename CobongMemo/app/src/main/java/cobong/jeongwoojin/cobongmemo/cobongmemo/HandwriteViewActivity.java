package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    ImageView deleteHandwrite;

    String handwriteId;

    final private static String root = Environment.getExternalStorageDirectory().toString();

    TextView handwriteTitle;
    TextView handwriteSubtitle;

    DBHelper helper;
    SQLiteDatabase db;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwrite_view);

        handwriteViewExit = (ImageView)findViewById(R.id.handwriteViewExit);
        handwriteViewImage =(ImageView)findViewById(R.id.handwriteViewImage);
        handwriteTitle = (TextView)findViewById(R.id.handwriteTitle);
        handwriteSubtitle = (TextView)findViewById(R.id.handwriteSubtitle);


        deleteHandwrite = (ImageView)findViewById(R.id.deletehandwrite);


        intent = getIntent();
        handwriteId = intent.getStringExtra("handwriteId");
        handwriteTitle.setText(intent.getStringExtra("title"));
        handwriteSubtitle.setText(intent.getStringExtra("subtitle"));

        Bitmap bitmap = BitmapFactory.decodeFile(root+"/saved_images/"+handwriteId+".jpg");


        handwriteViewImage.setImageBitmap(bitmap);



        deleteHandwrite.setOnClickListener(this);

        handwriteViewExit.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.handwriteViewExit:
                finish();
                break;


            case R.id.deletehandwrite:

                AlertDialog.Builder builder = new AlertDialog.Builder(HandwriteViewActivity.this);
                builder.setTitle("확인")
                        .setMessage("메모를 지우시겠습니까?")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delHandwrite(intent);
                            }
                        })
                        .setPositiveButton("취소",null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;

        }
    }


    public void delHandwrite(Intent intent){

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        int index = intent.getIntExtra("index",-1);
        if(index == -1)
            finish();

        String del = "delete from memo where `idx`="+index;
        db.execSQL(del);
        finish();

    }
}
