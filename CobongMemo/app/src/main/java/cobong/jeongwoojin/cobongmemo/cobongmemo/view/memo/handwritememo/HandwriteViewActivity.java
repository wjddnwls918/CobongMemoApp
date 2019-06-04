package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.DBHelper;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityHandwriteViewBinding;

public class HandwriteViewActivity extends AppCompatActivity implements HandwriteNavigator {

    private ActivityHandwriteViewBinding binding;
    private HandwriteViewModel viewModel;
  /*  ImageView handwriteViewExit;
    ImageView handwriteViewImage;
    ImageView deleteHandwrite;*/

    String handwriteId;

    final private static String root = Environment.getExternalStorageDirectory().toString();

   /* TextView handwriteTitle;
    TextView handwriteSubtitle;*/

    DBHelper helper;
    SQLiteDatabase db;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_handwrite_view);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_handwrite_view);

       /* handwriteViewExit = (ImageView)findViewById(R.id.handwriteViewExit);
        handwriteViewImage =(ImageView)findViewById(R.id.handwriteViewImage);
        handwriteTitle = (TextView)findViewById(R.id.handwriteTitle);
        handwriteSubtitle = (TextView)findViewById(R.id.handwriteSubtitle);


        deleteHandwrite = (ImageView)findViewById(R.id.deletehandwrite);*/

        viewModel = ViewModelProviders.of(this).get(HandwriteViewModel.class);
        viewModel.setNavigator(this);

        intent = getIntent();
        viewModel.setItem(intent.getParcelableExtra("handwriteItem"));


        binding.setViewmodel(viewModel);

       /* handwriteId = intent.getStringExtra("handwriteId");
        handwriteTitle.setText(intent.getStringExtra("title"));
        handwriteSubtitle.setText(intent.getStringExtra("subtitle"));*/

        Bitmap bitmap = BitmapFactory.decodeFile(root+"/saved_images/"+viewModel.getItem().getHandwriteId()+".jpg");


        binding.handwriteViewImage.setImageBitmap(bitmap);
        //handwriteViewImage.setImageBitmap(bitmap);



        //deleteHandwrite.setOnClickListener(this);

        //handwriteViewExit.setOnClickListener(this);
    }

    @Override
    public void onExitClick() {
        finish();
    }

    @Override
    public void onDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HandwriteViewActivity.this);
        builder.setTitle("확인")
                .setMessage("메모를 지우시겠습니까?")
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delHandwrite();
                    }
                })
                .setPositiveButton("취소",null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

   /* @Override
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
    }*/


    public void delHandwrite(){

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        /*int index = intent.getIntExtra("index",-1);
        if(index == -1)
            finish();*/

        String del = "delete from memo where `idx`="+ viewModel.getItem().getIndex();
        db.execSQL(del);
        finish();

    }
}
