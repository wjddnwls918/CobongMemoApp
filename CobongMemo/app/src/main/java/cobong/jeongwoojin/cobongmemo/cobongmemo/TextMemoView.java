package cobong.jeongwoojin.cobongmemo.cobongmemo;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class TextMemoView extends AppCompatActivity {

    ImageView exit;
    ImageView deletememo;
    ImageView editmemo;

    TextView viewtitle;
    TextView viewsubtitle;
    TextView viewcontent;


    DBHelper helper;
    SQLiteDatabase db;

    Intent intent;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_memo_view);

        viewtitle = findViewById(R.id.viewtitle);
        viewsubtitle = findViewById(R.id.viewsubtitle);
        viewcontent = findViewById(R.id.viewcontent);

        intent = getIntent();

        setView(intent);

        //뒤로 가기
        exit = findViewById(R.id.viewExit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //메모 지우기
        deletememo = findViewById(R.id.deletememo);
        deletememo.setOnClickListener(new View.OnClickListener() {
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
               .setPositiveButton("취소",null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


        //메모 수정
        editmemo = findViewById(R.id.editmemo);
        editmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TextMemoView.this,WriteMemoActivity.class);
                intent1.putExtra("index",intent.getIntExtra("index",-1));
                intent1.putExtra("type","editmemo");
                intent1.putExtra("title",viewtitle.getText().toString());
                intent1.putExtra("subtitle",viewsubtitle.getText().toString());
                intent1.putExtra("content",viewcontent.getText().toString());
                startActivityForResult(intent1,100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!= null){

            if(requestCode == 100 && resultCode == 101){
                Intent result = new Intent();
                result.putExtra("title",data.getStringExtra("title"));
                result.putExtra("subtitle",data.getStringExtra("subtitle"));
                result.putExtra("content",data.getStringExtra("content"));

                setView((result));

            }

        }


    }

    public void setView(Intent intent){
        String title = intent.getStringExtra("title");
        String subtitle = intent.getStringExtra("subtitle");
        String content = intent.getStringExtra("content");

        if(title.equals("")){
            viewtitle.setText("null");
        }else {
            viewtitle.setText(intent.getStringExtra("title"));
        }

        if(subtitle.equals("")){
            viewsubtitle.setText("null");
        }else {
            viewsubtitle.setText(intent.getStringExtra("subtitle"));
        }

        if(content.equals("")){
            viewcontent.setText("null");
        }else {
            viewcontent.setText(intent.getStringExtra("content"));
        }
    }

    public void delMemo(Intent intent){

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
