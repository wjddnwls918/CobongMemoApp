package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityTextWritingBinding;

public class TextMemoWriteActivity extends AppCompatActivity implements View.OnClickListener, TextMemoNavigator {

    Intent intent;

    private ActivityTextWritingBinding binding;
    private TextMemoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_writing);

        viewModel = ViewModelProviders.of(this).get(TextMemoViewModel.class);
        intent = getIntent();
        viewModel.setItem(intent.getParcelableExtra("textItem"));

        viewModel.setNavigator(this);
        binding.setViewmodel(viewModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                onBackPressed();
                break;
        }
    }

    //뒤로가기
    @Override
    public void onExitClick() {
        onBackPressed();
    }

    //작성하기
    @Override
    public void onWriteClick() {

        //키보드 숨기기
        KeyBoardUtil.hideSoftKeyboard(binding.getRoot(),getApplicationContext());

        //저장
        if (viewModel.getItem().getValue() == null) {
            if (binding.title.getText().toString().trim().equals("") && binding.content.getText().toString().trim().equals("")) {
                SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_title_content);
            } else if (binding.title.getText().toString().trim().equals("")) {
                SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_title);
            } else if (binding.content.getText().toString().trim().equals("")) {
                SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_content);
            } else {

                viewModel.insertTextMemo(binding.title.getText().toString(), binding.subTitle.getText().toString(), binding.content.getText().toString());
                finish();

            }
        } else {
            //수정
            if (binding.title.getText().toString().trim().equals("") && binding.content.getText().toString().trim().equals("")) {
                SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_title_content);
            } else if (binding.title.getText().toString().trim().equals("")) {
                SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_title);
            } else if (binding.content.getText().toString().trim().equals("")) {
                SnackBarUtil.showSnackBar(binding.getRoot(), R.string.text_input_content);
            } else {

                //update text memo
                viewModel.updateTextMemo(viewModel.getItem().getValue().getIndex(),binding.title.getText().toString(), binding.subTitle.getText().toString(), binding.content.getText().toString());

                viewModel.getItem().getValue().setTitle(binding.title.getText().toString());
                viewModel.getItem().getValue().setSubTitle(binding.subTitle.getText().toString());
                viewModel.getItem().getValue().setContent(binding.content.getText().toString());

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
