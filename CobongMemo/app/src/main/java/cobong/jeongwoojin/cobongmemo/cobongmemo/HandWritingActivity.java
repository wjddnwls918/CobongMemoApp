package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class HandWritingActivity extends AppCompatActivity implements View.OnClickListener{

    DrawableViewConfig config;
    EditText title;
    EditText subTitle;


    ImageView insert;
    ImageView exit;

    DBHelper helper;
    SQLiteDatabase db;
    DrawableView drawableView;

    ImageView color;
    ImageView widthUp;
    ImageView widthDown;
    ImageView eraser;
    ImageView undo;

    String handwriteId;

    boolean erase;

    int canHeight,canWidth;
    int temcol;

    final private static String root = Environment.getExternalStorageDirectory().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_writing);

        erase = false;

        drawableView = (DrawableView)findViewById(R.id.paintView);
        title = (EditText)findViewById(R.id.handwriteTitle);
        subTitle = (EditText)findViewById(R.id.handwriteSubtitle);

        exit = (ImageView)findViewById(R.id.handwriteExit);
        insert = (ImageView)findViewById(R.id.handwriteInsert);

        color = (ImageView)findViewById(R.id.handwriteColor);
        widthUp = (ImageView)findViewById(R.id.widthUp);
        widthDown = (ImageView)findViewById(R.id.widthDown);
        eraser = (ImageView)findViewById(R.id.eraser);
        undo = (ImageView)findViewById(R.id.undo);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;


        Toast.makeText(this,"canHeight : "+height+" canWidth : "+width,Toast.LENGTH_LONG).show();


        config = new DrawableViewConfig();
        config.setStrokeColor(getResources().getColor(android.R.color.black));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);
        config.setCanvasHeight(1000);
        config.setCanvasWidth(width-20);
        drawableView.setConfig(config);

        //java drawableView.obtainBitmap()

        exit.setOnClickListener(this);
        insert.setOnClickListener(this);
        color.setOnClickListener(this);
        widthUp.setOnClickListener(this);
        widthDown.setOnClickListener(this);
        eraser.setOnClickListener(this);
        undo.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.handwriteExit:
                onBackPressed();
                break;

            case R.id.handwriteInsert:


                if (title.getText().toString().trim().equals("") && subTitle.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "제목과 소제목을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (title.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (subTitle.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "소제목을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {

                    writeDBvoice();
                    finish();
                }

                break;

            case R.id.handwriteColor:

                /*config.setStrokeColor(
                        Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));*/


               /* getFragmentManager().beginTransaction()
                        .add(android.R.id.content, new ColorFragment())
                        .commit();*/

                /*Intent intent = new Intent(this,ColorSelectActivity.class);
                startActivity(intent);*/
                break;

            case R.id.widthUp:
                config.setStrokeWidth(config.getStrokeWidth() + 10);
                break;

            case R.id.widthDown:
                config.setStrokeWidth(config.getStrokeWidth() - 10);
                break;

            case R.id.eraser:
                if (erase) {

                    config.setStrokeColor(temcol);
                    config.setStrokeWidth(20.0f);
                    erase = false;

                } else {
                    temcol = config.getStrokeColor();

                    config.setStrokeColor(Color.argb(255, 255, 255, 255));
                    config.setStrokeWidth(40.0f);
                    erase = true;
                }

                break;

            case R.id.undo:
                drawableView.undo();
                break;
        }
    }

    public void writeDBvoice(){
        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

         /*      String memoSQL = "create table "+ TABLE_MEMO +
                "(idx integer not null primary key autoincrement,"+
                "  INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                "TITLE not null,"+
                "SUBTITLE TEXT DEFAULT '',"+
                "CONTENT DEFAULT '' , " +
                " MEMO_TYPE text default '', "+
                " ID_HANDWRITING TEXT, "+
                " ID_VOICE TEXT "+
                ")";*/


         File myDir = new File(root+"/saved_images");
         myDir.mkdir();
        handwriteId = curDate();
        String fname = handwriteId+".jpg";



        FileOutputStream out = null;
        try{

            Bitmap image = drawableView.obtainBitmap();

            Canvas canvas = new Canvas(image);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(drawableView.obtainBitmap(), 0, 0, null);

            out = new FileOutputStream(new File(myDir,fname),true);
            image.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.flush();
            out.close();
        }catch ( Exception e){
            e.printStackTrace();
        }



        String insertHandwrite;
        Toast.makeText(this,handwriteId,Toast.LENGTH_SHORT).show();
        insertHandwrite = "insert into memo(title,SUBTITLE,MEMO_TYPE,ID_HANDWRITING) values(?,?,?,?)";
        String args[] = {title.getText().toString(),subTitle.getText().toString(), "handwrite", handwriteId};
        try {
            db.execSQL(insertHandwrite,args);
            Toast.makeText(this,"입력 완료",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"입력 실패",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?");
        builder.setPositiveButton("확인", dialogListener);
        builder.setNegativeButton("취소", null);
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    };


    public String curDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String getTime = sdf.format(date);

        return getTime;
    }




}
