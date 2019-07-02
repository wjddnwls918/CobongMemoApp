package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityTextWritingBinding

class TextMemoWriteActivity : AppCompatActivity(), View.OnClickListener, TextMemoNavigator {

    private lateinit var binding: ActivityTextWritingBinding
    private lateinit var viewModel: TextMemoViewModel

    internal var dialogListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ -> finish() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_writing)

        viewModel = ViewModelProviders.of(this).get(TextMemoViewModel::class.java)

        viewModel.item.value = intent.getParcelableExtra("textItem")

        viewModel.navigator = this
        binding.viewmodel = viewModel
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.exit -> onBackPressed()
        }
    }

    //뒤로가기
    override fun onExitClick() {
        onBackPressed()
    }

    //작성하기
    override fun onWriteClick() {

        //키보드 숨기기
        KeyBoardUtil.hideSoftKeyboard(binding.root, applicationContext)

        //저장
        if (viewModel.item.value == null) {
            if (binding.title.text!!.toString().trim { it <= ' ' } == "" && binding.content.text!!.toString().trim { it <= ' ' } == "") {
                SnackBarUtil.showSnackBar(binding.root, R.string.text_input_title_content)
            } else if (binding.title.text.toString().trim { it <= ' ' } == "") {
                SnackBarUtil.showSnackBar(binding.root, R.string.text_input_title)
            } else if (binding.content.text!!.toString().trim { it <= ' ' } == "") {
                SnackBarUtil.showSnackBar(binding.root, R.string.text_input_content)
            } else {

                viewModel.insertTextMemo(
                    binding.title.text!!.toString(),
                    binding.subTitle.text!!.toString(),
                    binding.content.text!!.toString()
                )
                finish()

            }
        } else {
            //수정
            if (binding.title.text!!.toString().trim { it <= ' ' } == "" && binding.content.text!!.toString().trim { it <= ' ' } == "") {
                SnackBarUtil.showSnackBar(binding.root, R.string.text_input_title_content)
            } else if (binding.title.text!!.toString().trim { it <= ' ' } == "") {
                SnackBarUtil.showSnackBar(binding.root, R.string.text_input_title)
            } else if (binding.content.text!!.toString().trim { it <= ' ' } == "") {
                SnackBarUtil.showSnackBar(binding.root, R.string.text_input_content)
            } else {

                //update text memo
                viewModel.updateTextMemo(
                    viewModel.item.value!!.index,
                    binding.title.text!!.toString(),
                    binding.subTitle.text!!.toString(),
                    binding.content.text!!.toString()
                )

                viewModel.item.value?.title = binding.title.text.toString()
                viewModel.item.value?.subTitle = binding.subTitle.text.toString()
                viewModel.item.value?.content = binding.content.text.toString()


                val intent = Intent()
                intent.putExtra("result",viewModel.item.value)


                setResult(101,intent)
                finish()

            }
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?")
        builder.setPositiveButton("확인", dialogListener)
        builder.setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}
