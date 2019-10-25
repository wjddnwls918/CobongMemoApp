package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityTextViewBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem


class TextMemoViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextViewBinding
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: TextMemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(TextMemoViewModel::class.java).apply {
                item.value = intent.getParcelableExtra("textItem")
            }

        binding = DataBindingUtil.setContentView<ActivityTextViewBinding>(this, R.layout.activity_text_view).apply {
            viewmodel = viewModel
        }

        setupNavigation()
    }

    private fun setupNavigation() {

        //뒤로 가기
        viewModel.exitClickEvent.observe(this, EventObserver {
            finish()
        })

        //작성하기
        viewModel.editMemoEvent.observe(this, EventObserver {
            onEditClick()
        })

        //삭제
        viewModel.deleteMemoEvent.observe(this, EventObserver {
            onDeleteClick()
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if ( (requestCode == 100 && resultCode == 101) && data != null) {

            var item = data.getParcelableExtra<MemoItem>("result")

            binding.viewtitle.setText(item.title)
            binding.viewsubtitle.setText(item.subTitle)
            binding.viewcontent.setText(item.content)

            viewModel.item.value?.title = item.title
            viewModel.item.value?.subTitle = item.subTitle
            viewModel.item.value?.content = item.content
        }
    }

    fun onExitClick() {
        finish()
    }

    //메모 수정
    fun onEditClick() {
        val intent = Intent(this@TextMemoViewActivity, TextMemoWriteActivity::class.java)
        intent.putExtra("textItem", viewModel.item.value)
        startActivityForResult(intent, 100)
    }

    //메모 삭제
    fun onDeleteClick() {
        val builder = AlertDialog.Builder(this@TextMemoViewActivity)
        builder.setTitle("확인")
            .setMessage("메모를 지우시겠습니까?")
            .setNegativeButton("확인") { _, _ ->
                //viewModel.deleteTextMemo()

                viewModel.deleteTextMemoByRoom()
                finish()
            }
            .setPositiveButton("취소", null)

        val alertDialog = builder.create()
        alertDialog.show()
    }
}
