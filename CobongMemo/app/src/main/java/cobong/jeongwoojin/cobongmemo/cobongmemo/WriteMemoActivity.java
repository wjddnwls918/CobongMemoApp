package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


//Style 적용하는데 AppCompatActivity가 안되는데 ??
public class WriteMemoActivity extends Activity implements View.OnClickListener {

    EditText title;
    EditText subTitle;
    EditText content;

    ImageView exit;
    ImageView writeMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_memo);

        title = (EditText)findViewById(R.id.title);
        subTitle = (EditText)findViewById(R.id.subtitle);
        content = (EditText)findViewById(R.id.content);

        exit = (ImageView)findViewById(R.id.exit);
        exit.setOnClickListener(this);
        writeMemo = (ImageView) findViewById(R.id.writeMemo);
        writeMemo.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?");
        builder.setPositiveButton("확인", dialogListener);
        builder.setNegativeButton("취소", null);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.exit:
                onBackPressed();
                break;
            case R.id.writeMemo:
                Toast.makeText(this,"메모 입력 버튼입니다. \n"+title.getText()+"\n"+subTitle.getText()+"\n"+content.getText(),Toast.LENGTH_SHORT).show();

                DBHelper helper = new DBHelper(this);
                break;

        }
    }




}
