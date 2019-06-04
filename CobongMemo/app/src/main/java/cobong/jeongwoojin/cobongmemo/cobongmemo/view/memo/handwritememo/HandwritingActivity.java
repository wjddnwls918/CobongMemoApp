package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.DBHelper;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityHandWritingBinding;
import me.panavtec.drawableview.DrawableViewConfig;

public class HandwritingActivity extends AppCompatActivity implements View.OnClickListener, ColorPickerDialogListener {


    DrawableViewConfig config;
  /* EditText title;
    EditText subTitle;


    ImageView insert;
    ImageView exit;
*/

    DBHelper helper;
    SQLiteDatabase db;
  //  DrawableView drawableView;

/*
    ImageView color;
    ImageView widthUp;
    ImageView widthDown;
    ImageView eraser;
    ImageView undo;
    ImageView blackpencil;
*/

    String handwriteId;

    boolean erase;

    int canHeight, canWidth;
    int temcol;

  /*  LinearLayout linearImage;*/

    String type;

    final private static String root = Environment.getExternalStorageDirectory().toString();

    private ActivityHandWritingBinding binding;

    HandwriteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hand_writing);
        //setContentView(R.layout.activity_hand_writing);

        viewModel = ViewModelProviders.of(this).get(HandwriteViewModel.class);


        erase = false;

        binding.setViewmodel(viewModel);

       /* drawableView = (DrawableView) findViewById(R.id.paintView);
        title = (EditText) findViewById(R.id.handwriteTitle);
        subTitle = (EditText) findViewById(R.id.handwriteSubtitle);

        exit = (ImageView) findViewById(R.id.handwriteExit);
        insert = (ImageView) findViewById(R.id.handwriteInsert);

        color = (ImageView) findViewById(R.id.handwriteColor);
        widthUp = (ImageView) findViewById(R.id.widthUp);
        widthDown = (ImageView) findViewById(R.id.widthDown);
        eraser = (ImageView) findViewById(R.id.eraser);
        undo = (ImageView) findViewById(R.id.undo);
        blackpencil = (ImageView) findViewById(R.id.blackPencil);*/

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        config = new DrawableViewConfig();
        config.setStrokeColor(getResources().getColor(android.R.color.black));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);
        config.setCanvasHeight(1000);
        config.setCanvasWidth(width - 20);


        binding.paintView.setConfig(config);


        Intent intent = getIntent();
        viewModel.setItem(intent.getParcelableExtra("handwriteItem"));


        type = intent.getStringExtra("type");
        if (viewModel.getItem()!= null) {

            //drawableView.set
            //Toast.makeText(this, intent.getStringExtra("type"), Toast.LENGTH_LONG).show();
            //handwriteId = intent.getStringExtra("handwriteId");

            Bitmap bitmap = BitmapFactory.decodeFile(root + "/saved_images/" + viewModel.getItem().getHandwriteId() + ".jpg");
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

            binding.paintView.setBackground(bitmapDrawable);
/*
            title.setText(intent.getStringExtra("title"));
            subTitle.setText(intent.getStringExtra("subTitle"));*/

        }

/*

        exit.setOnClickListener(this);
        insert.setOnClickListener(this);
        color.setOnClickListener(this);
        widthUp.setOnClickListener(this);
        widthDown.setOnClickListener(this);
        eraser.setOnClickListener(this);
        undo.setOnClickListener(this);
        blackpencil.setOnClickListener(this);
*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.handwriteExit:
                onBackPressed();
                break;

            case R.id.handwriteInsert:


              /*  if (title.getText().toString().trim().equals("") && subTitle.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "제목과 소제목을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (title.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (subTitle.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "소제목을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {


                    if (!type.equals("insert")) {
                        writeSDCARD();
                    } else {
                        writeDBhandwrite();

                    }
                    finish();
                }*/

                break;

            case R.id.handwriteColor:
                //choice color
                ColorPickerDialog.newBuilder()
                        .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                        .setAllowPresets(false)
                        .setDialogId(0)
                        .setColor(Color.BLACK)
                        .setShowAlphaSlider(true)
                        .show(this);
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
                binding.paintView.undo();
                break;


            case R.id.blackPencil:
                config.setStrokeColor(Color.argb(255, 0, 0, 0));
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {

        temcol = color;
        config.setStrokeColor(temcol);

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    public void writeSDCARD() {
        File editFile = new File(root + "/saved_images/" + handwriteId + ".jpg");
        try {
            FileOutputStream out;
            Bitmap back = ((BitmapDrawable) binding.paintView.getBackground().getCurrent()).getBitmap();
            Bitmap image = binding.paintView.obtainBitmap();


            //merge bitmap
            //아 .. 진ㅉ ㅏ오래 걸림
            Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
            Canvas canvas2 = new Canvas(result);
            int widthBack = back.getWidth();
            int widthFront = image.getWidth();
            float move = (widthBack - widthFront) / 2;
            canvas2.drawBitmap(back, 0f, 0f, null);
            canvas2.drawBitmap(image, move, move, null);


            out = new FileOutputStream(editFile, false);
            result.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

            //Toast.makeText(this, "이미지 저장 완료", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writeDBhandwrite() {
        helper = new DBHelper(this);
        db = helper.getWritableDatabase();


        File myDir = new File(root + "/saved_images");
        myDir.mkdir();
        handwriteId = curDate();
        String fname = handwriteId + ".jpg";


        FileOutputStream out = null;
        try {

            Bitmap image = binding.paintView.obtainBitmap();

            Canvas canvas = new Canvas(image);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(binding.paintView.obtainBitmap(), 0, 0, null);

            out = new FileOutputStream(new File(myDir, fname), true);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String insertHandwrite;
        Toast.makeText(this, handwriteId, Toast.LENGTH_SHORT).show();
        insertHandwrite = "insert into memo(title,SUBTITLE,MEMO_TYPE,ID_HANDWRITING) values(?,?,?,?)";
        String args[] = {binding.handwriteTitle.getText().toString(), binding.handwriteSubtitle.getText().toString(), "handwrite", handwriteId};
        try {
            db.execSQL(insertHandwrite, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?");
        builder.setPositiveButton("확인", dialogListener);
        builder.setNegativeButton("취소", null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    };


    public String curDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String getTime = sdf.format(date);

        return getTime;
    }


}
