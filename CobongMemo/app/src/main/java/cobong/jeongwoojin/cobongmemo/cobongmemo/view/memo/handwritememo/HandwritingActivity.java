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
import android.util.DisplayMetrics;
import android.view.View;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.io.File;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BasicInfo;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityHandWritingBinding;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.DBHelper;
import me.panavtec.drawableview.DrawableViewConfig;

public class HandwritingActivity extends AppCompatActivity implements View.OnClickListener, ColorPickerDialogListener, HandwriteNavigator {

    private DrawableViewConfig config;

    DBHelper helper;
    SQLiteDatabase db;

    private String handwriteId;
    private boolean erase;
    private int temcol;
    private String type;

    private ActivityHandWritingBinding binding;
    private HandwriteViewModel viewModel;
    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hand_writing);

        viewModel = ViewModelProviders.of(this).get(HandwriteViewModel.class);
        viewModel.setNavigator(this);

        erase = false;

        binding.setViewmodel(viewModel);


        //읽어와서 저장
        Intent intent = getIntent();
        viewModel.setItem(intent.getParcelableExtra("handwriteItem"));

        //캔버스 초기화
        initCanvas();

        //작성인지 수정인지 설정
        type = intent.getStringExtra("type");

        //수정이면
        if (type.equals("edit")) {
            binding.handwriteTitle.setKeyListener(null);
            binding.handwriteSubtitle.setKeyListener(null);
        }

        if (viewModel.getItem() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(BasicInfo.root + "/saved_images/" + viewModel.getItem().getHandwriteId() + ".jpg");
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            binding.paintView.setBackground(bitmapDrawable);
        }

        //클릭리스너 등록
        initClickListener();

    }


    public void initCanvas() {
        DisplayMetrics dm = getResources().getDisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = (int) (dm.heightPixels * 0.65);

        //Log.d("sizecheck", width + " " + height);

        config = new DrawableViewConfig();
        config.setStrokeColor(getResources().getColor(android.R.color.black));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);

        config.setCanvasHeight(height);
        config.setCanvasWidth(width);

        binding.paintView.setConfig(config);
    }

    public void initClickListener() {
        binding.handwriteColor.setOnClickListener(this);
        binding.widthUp.setOnClickListener(this);
        binding.widthDown.setOnClickListener(this);
        binding.eraser.setOnClickListener(this);
        binding.undo.setOnClickListener(this);
        binding.blackPencil.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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

                temcol = config.getStrokeColor();
                config.setStrokeColor(Color.argb(255, 255, 255, 255));
                config.setStrokeWidth(40.0f);

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

    //수정시
    public void editHandwrite() {

        File editFile = new File(BasicInfo.root + "/saved_images/" + viewModel.getItem().getHandwriteId() + ".jpg");
        try {
            FileOutputStream out;
            Bitmap back = ((BitmapDrawable) binding.paintView.getBackground().getCurrent()).getBitmap();
            Bitmap image = binding.paintView.obtainBitmap();


            //merge bitmap
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //처음 작성 시
    public void insertHandWrite() {
        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        File myDir = new File(BasicInfo.root + "/saved_images");
        myDir.mkdir();
        handwriteId = DateUtil.curDate();
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

    //나가기
    @Override
    public void onExitClick() {
        onBackPressed();
    }

    //작성
    @Override
    public void onWriteClick() {

        KeyBoardUtil.hideSoftKeyboard(binding.getRoot(), this);

        if (binding.handwriteTitle.getText().toString().trim().equals("") && binding.handwriteSubtitle.getText().toString().trim().equals("")) {
            SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_title_subTitle);
        } else if (binding.handwriteTitle.getText().toString().trim().equals("")) {
            SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_title);
        } else if (binding.handwriteSubtitle.getText().toString().trim().equals("")) {
            SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_subTitle);
        } else {

            if (!type.equals("insert")) {
                editHandwrite();
            } else {
                insertHandWrite();

            }
            finish();
        }
    }
}
