package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityTextViewBinding


class TextMemoViewActivity : AppCompatActivity(), TextMemoNavigator {

    //Intent intent;
    private var binding: ActivityTextViewBinding? = null
    private var viewModel: TextMemoViewModel? = null

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_view)
        //intent = getIntent();
        viewModel = ViewModelProviders.of(this).get(TextMemoViewModel::class.java)

        viewModel!!.item!!.setValue(if (intent.getParcelableExtra("textItem"));)
        null

        //네비게이터 지정
        viewModel!!.navigator = this

        binding!!.viewmodel = viewModel

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

    override fun onExitClick() {
        finish()
    }


    //메모 수정
    override fun onEditClick() {
        val intent = Intent(this@TextMemoViewActivity, TextMemoWriteActivity::class.java)
        intent.putExtra("textItem", viewModel!!.item!!.value)
        startActivityForResult(intent, 100)
    }

    //메모 삭제
    override fun onDeleteClick() {
        val builder = AlertDialog.Builder(this@TextMemoViewActivity)
        builder.setTitle("확인")
            .setMessage("메모를 지우시겠습니까?")
            .setNegativeButton("확인") { dialog, which ->
                viewModel!!.deleteTextMemo()
                finish()
            }
            .setPositiveButton("취소", null)

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onWriteClick() {

    }
}
