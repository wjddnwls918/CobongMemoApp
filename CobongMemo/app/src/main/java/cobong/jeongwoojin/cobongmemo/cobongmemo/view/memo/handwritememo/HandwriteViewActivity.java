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
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.DBHelper;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityHandwriteViewBinding;

public class HandwriteViewActivity extends AppCompatActivity implements HandwriteNavigator {

    private ActivityHandwriteViewBinding binding;
    private HandwriteViewModel viewModel;

    String handwriteId;

    final private static String root = Environment.getExternalStorageDirectory().toString();

    DBHelper helper;
    SQLiteDatabase db;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_handwrite_view);

        viewModel = ViewModelProviders.of(this).get(HandwriteViewModel.class);
        viewModel.setNavigator(this);

        //
        intent = getIntent();
        viewModel.setItem(intent.getParcelableExtra("handwriteItem"));

        binding.setViewmodel(viewModel);


        Bitmap bitmap = BitmapFactory.decodeFile(root+"/saved_images/"+viewModel.getItem().getHandwriteId()+".jpg");


        binding.handwriteViewImage.setImageBitmap(bitmap);

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
