package cobong.jeongwoojin.cobongmemo.cobongmemo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityTextMemoViewBinding;


public class TextMemoView extends AppCompatActivity {

    DBHelper helper;
    SQLiteDatabase db;

    Intent intent;

    private ActivityTextMemoViewBinding binding;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_memo_view);

        intent = getIntent();

        setView(intent);

        //뒤로 가기
        binding.viewExit.setOnClickListener( (v) -> finish() );

        //메모 지우기
        binding.deletememo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TextMemoView.this);
                builder.setTitle("확인")
                        .setMessage("메모를 지우시겠습니까?")
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delMemo(intent);
                            }
                        })
                        .setPositiveButton("취소", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


        //메모 수정
        binding.editmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TextMemoView.this, WriteMemoActivity.class);
                intent1.putExtra("index", intent.getIntExtra("index", -1));
                intent1.putExtra("type", "editmemo");
                intent1.putExtra("title", binding.viewtitle.getText().toString());
                intent1.putExtra("subtitle", binding.viewsubtitle.getText().toString());
                intent1.putExtra("content", binding.viewcontent.getText().toString());
                startActivityForResult(intent1, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            if (requestCode == 100 && resultCode == 101) {
                Intent result = new Intent();
                result.putExtra("title", data.getStringExtra("title"));
                result.putExtra("subtitle", data.getStringExtra("subtitle"));
                result.putExtra("content", data.getStringExtra("content"));

                setView((result));

            }

        }


    }

    public void setView(Intent intent) {
        String title = intent.getStringExtra("title");
        String subtitle = intent.getStringExtra("subtitle");
        String content = intent.getStringExtra("content");

        if (title.equals("")) {
            binding.viewtitle.setText("null");
        } else {
            binding.viewtitle.setText(intent.getStringExtra("title"));
        }

        if (subtitle.equals("")) {
            binding.viewsubtitle.setText("null");
        } else {
            binding.viewsubtitle.setText(intent.getStringExtra("subtitle"));
        }

        if (content.equals("")) {
            binding.viewcontent.setText("null");
        } else {
            binding.viewcontent.setText(intent.getStringExtra("content"));
        }
    }

    public void delMemo(Intent intent) {

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        int index = intent.getIntExtra("index", -1);
        if (index == -1)
            finish();

        String del = "delete from memo where `idx`=" + index;
        db.execSQL(del);
        finish();

    }
}
