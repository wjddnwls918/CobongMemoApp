package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.DBHelper;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityTextViewBinding;


public class TextMemoViewActivity extends AppCompatActivity implements TextMemoNavigator {

    DBHelper helper;
    SQLiteDatabase db;

    Intent intent;

    private ActivityTextViewBinding binding;
    private TextMemoViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();

        //Toast.makeText(this,viewModel.getItem().getTitle().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_view);
        intent = getIntent();
        viewModel = ViewModelProviders.of(this).get(TextMemoViewModel.class);

        viewModel.setItem(intent.getParcelableExtra("textItem"));

        //네비게이터 지정
        viewModel.setNavigator(this);

        binding.setViewmodel(viewModel);



        //뒤로 가기
        binding.viewExit.setOnClickListener((v) -> finish());

    }

    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            if (requestCode == 100 && resultCode == 101) {
                Intent result = getIntent();
                MemoListItem item = result.getParcelableExtra("textItem");
                binding.viewtitle.setText(item.getTitle());
                binding.viewsubtitle.setText(item.getSubTitle());
                binding.viewcontent.setText(item.getContent());
            }
        }


    }*/

    @Override
    public void onExitClick() {
        finish();
    }

    public void delMemo() {

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        String del = "delete from memo where `idx`=" + viewModel.getItem().getValue().getIndex();
        db.execSQL(del);
        finish();

    }

    //메모 수정
    @Override
    public void onEditClick() {
        Intent intent = new Intent(TextMemoViewActivity.this, TextMemoWriteActivity.class);
        intent.putExtra("textItem", viewModel.getItem().getValue());
        startActivityForResult(intent, 100);
    }

    //메모 삭제
    @Override
    public void onDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TextMemoViewActivity.this);
        builder.setTitle("확인")
                .setMessage("메모를 지우시겠습니까?")
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delMemo();
                    }
                })
                .setPositiveButton("취소", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
