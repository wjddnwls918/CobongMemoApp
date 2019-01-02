package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.Toast;


import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    ImageView blackpencil;

    String handwriteId;

    boolean erase;

    int canHeight,canWidth;
    int temcol;

    LinearLayout linearImage;

    String type;

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
        blackpencil = (ImageView)findViewById(R.id.blackPencil);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;


        //Toast.makeText(this,"canHeight : "+height+" canWidth : "+width,Toast.LENGTH_LONG).show();


        config = new DrawableViewConfig();
        config.setStrokeColor(getResources().getColor(android.R.color.black));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);
        config.setCanvasHeight(1000);
        config.setCanvasWidth(width-20);



        drawableView.setConfig(config);


        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(!type.equals("insert")){

            //drawableView.set
            Toast.makeText(this,intent.getStringExtra("type"),Toast.LENGTH_LONG).show();
            handwriteId = intent.getStringExtra("handwriteId");

            Bitmap bitmap = BitmapFactory.decodeFile(root+"/saved_images/"+handwriteId+".jpg");
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

            drawableView.setBackground(bitmapDrawable);


            //drawableView.draw(canvas);
            //drawableView.add

            //drawableView.draw(myCanvas);

           // drawableView.

            title.setText(intent.getStringExtra("title"));
            subTitle.setText(intent.getStringExtra("subTitle"));

        }



        exit.setOnClickListener(this);
        insert.setOnClickListener(this);
        color.setOnClickListener(this);
        widthUp.setOnClickListener(this);
        widthDown.setOnClickListener(this);
        eraser.setOnClickListener(this);
        undo.setOnClickListener(this);
        blackpencil.setOnClickListener(this);


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


                    if(!type.equals("insert")) {
                        writeSDCARD();
                    }else {
                        writeDBhandwrite();

                    }
                    finish();
                }

                break;

            case R.id.handwriteColor:


                ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this);
                builder.setTitle(R.string.colorSelect);
                builder.setPreferenceName("MyColorPickerDialog");
                builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
                builder.setPositiveButton(getString(R.string.confirm), new ColorListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope colorEnvelope) {
                     /*   TextView textView = findViewById(R.id.textView);
                        textView.setText("#" + colorEnvelope.getHtmlCode());

                        LinearLayout linearLayout = findViewById(R.id.linearLayout);
                        linearLayout.setBackgroundColor(colorEnvelope.getColor());*/
                     temcol = colorEnvelope.getColor();
                     config.setStrokeColor(temcol);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();



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


            case R.id.blackPencil:
                config.setStrokeColor(Color.argb(255,0,0,0));
        }
    }

    public void writeSDCARD(){
        File editFile = new File(root+"/saved_images/"+handwriteId+".jpg");
        try {
            FileOutputStream out;
            Bitmap back = ((BitmapDrawable)drawableView.getBackground().getCurrent()).getBitmap();
            Bitmap image = drawableView.obtainBitmap();


            //merge bitmap
            //아 .. 진ㅉ ㅏ오래 걸림
            Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
            Canvas canvas2 = new Canvas(result);
            int widthBack = back.getWidth();
            int widthFront = image.getWidth();
            float move = (widthBack - widthFront) / 2;
            canvas2.drawBitmap(back, 0f, 0f, null);
            canvas2.drawBitmap(image, move, move, null);


            out = new FileOutputStream(editFile,false);
            result.compress(Bitmap.CompressFormat.JPEG,100,out);

            out.flush();
            out.close();

                Toast.makeText(this,"이미지 저장 완료",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void writeDBhandwrite(){
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
