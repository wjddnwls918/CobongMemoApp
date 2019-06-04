package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.DBHelper;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityWriteMemoBinding;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;


//Style 적용하는데 AppCompatActivity가 안되는데 ??
public class TextMemoWriteActivity extends AppCompatActivity implements View.OnClickListener, TextMemoNavigator {

/*    EditText title;
    EditText subTitle;
    EditText content;

    ImageView exit;
    ImageView writeMemo;*/

    SQLiteDatabase db;
    Cursor cursor;

    Intent intent;

    private ActivityWriteMemoBinding binding;
    private TextMemoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_write_memo);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_memo);
        viewModel = ViewModelProviders.of(this).get(TextMemoViewModel.class);

      /*  title = (EditText)findViewById(R.id.title);
        subTitle = (EditText)findViewById(R.id.subtitle);
        content = (EditText)findViewById(R.id.content);*/

        intent = getIntent();
        viewModel.setItem(intent.getParcelableExtra("textItem"));
        viewModel.setNavigator(this);

        binding.setViewmodel(viewModel);

        /*if (viewModel.getItem() != null)
            Toast.makeText(this, viewModel.getItem().getTitle(), Toast.LENGTH_SHORT).show();
*/

       /* if(intent.getStringExtra("type").equals("editmemo")){
            String editTitle = intent.getStringExtra("title");
            String editSubTitle = intent.getStringExtra("subtitle");
            String editContent = intent.getStringExtra("content");

            title.setText(editTitle);
            subTitle.setText(editSubTitle);
            content.setText(editContent);

        }

        exit = (ImageView)findViewById(R.id.exit);
        exit.setOnClickListener(this);
        writeMemo = (ImageView) findViewById(R.id.writeMemo);
        writeMemo.setOnClickListener(this);*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                onBackPressed();
                break;
            case R.id.writeMemo:
                //Toast.makeText(this,"메모 입력 버튼입니다. \n"+title.getText()+"\n"+subTitle.getText()+"\n"+content.getText(),Toast.LENGTH_SHORT).show();

                /*if(intent.getStringExtra("type").equals("addmemo")) {
                    if (title.getText().toString().trim().equals("") && content.getText().toString().trim().equals("")) {
                        Toast.makeText(this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    } else if (title.getText().toString().trim().equals("")) {
                        Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    } else if (content.getText().toString().trim().equals("")) {
                        Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    } else {

                        DBHelper helper = new DBHelper(this);
                        db = helper.getWritableDatabase();

                        String insertMemo;
                        insertMemo = "insert into memo(title,subtitle,content,memo_type) values(?,?,?,?)";
                        String args[] = {title.getText().toString(), subTitle.getText().toString(), content.getText().toString(), "text"};
                        db.execSQL(insertMemo, args);
                        finish();
                        break;
                    }
                }else{
                    //Toast.makeText(this,"edit 메모 입니다.",Toast.LENGTH_SHORT).show();

                    int index = intent.getIntExtra("index",-1);

                    if(index != -1) {
                        if (title.getText().toString().trim().equals("") && content.getText().toString().trim().equals("")) {
                            Toast.makeText(this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
                        } else if (title.getText().toString().trim().equals("")) {
                            Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                        } else if (content.getText().toString().trim().equals("")) {
                            Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                        } else {

                            DBHelper helper = new DBHelper(this);
                            db = helper.getWritableDatabase();

                            String updateMemo;
                            updateMemo = "update memo set title=?,subtitle=?,content=? where idx="+index;
                            String args[] = {title.getText().toString(), subTitle.getText().toString(), content.getText().toString()};
                            db.execSQL(updateMemo, args);


                            //결과 반환
                            Intent result = new Intent(this, TextMemoView.class);
                            result.putExtra("title",title.getText().toString());
                            result.putExtra("subtitle",subTitle.getText().toString());
                            result.putExtra("content",content.getText().toString());

                            setResult(101,result);


                            finish();
                            break;
                        }
                    }
                }*/


        }
    }


    @Override
    public void onExitClick() {
        onBackPressed();
    }

    @Override
    public void onWriteClick() {

        DBHelper helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        //저장
        if (viewModel.getItem() == null) {
            if (binding.title.getText().toString().trim().equals("") && binding.content.getText().toString().trim().equals("")) {
                Toast.makeText(this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (binding.title.getText().toString().trim().equals("")) {
                Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (binding.content.getText().toString().trim().equals("")) {
                Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {


                String insertMemo;
                insertMemo = "insert into memo(title,subtitle,content,memo_type) values(?,?,?,?)";
                String args[] = {binding.title.getText().toString(), binding.subTitle.getText().toString(), binding.content.getText().toString(), "text"};
                db.execSQL(insertMemo, args);
                finish();

            }
        } else {
            //수정
            if (binding.title.getText().toString().trim().equals("") && binding.content.getText().toString().trim().equals("")) {
                Toast.makeText(this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (binding.title.getText().toString().trim().equals("")) {
                Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (binding.content.getText().toString().trim().equals("")) {
                Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {

                String updateMemo;
                updateMemo = "update memo set title=?,subtitle=?,content=? where idx=" + viewModel.getItem().getIndex();
                String args[] = {binding.title.getText().toString(), binding.subTitle.getText().toString(), binding.content.getText().toString()};
                db.execSQL(updateMemo, args);


                //결과 반환
                Intent result = new Intent(this, TextMemoView.class);

                MemoListItem resultItem = viewModel.getItem();
                resultItem.setTitle(binding.title.getText().toString());
                resultItem.setSubTitle(binding.subTitle.getText().toString());
                resultItem.setContent(binding.content.getText().toString());

                result.putExtra("textItem",resultItem);

                /*result.putExtra("title", title.getText().toString());
                result.putExtra("subtitle", subTitle.getText().toString());
                result.putExtra("content", content.getText().toString());*/

                setResult(101, result);


                finish();


            }
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

}
