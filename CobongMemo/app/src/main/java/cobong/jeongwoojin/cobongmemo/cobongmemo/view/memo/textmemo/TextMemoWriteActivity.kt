package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityTextWritingBinding

class TextMemoWriteActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityTextWritingBinding
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: TextMemoViewModel

    internal var dialogListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ -> finish() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(TextMemoViewModel::class.java).apply {
                item.value = intent.getParcelableExtra("textItem")
            }

        binding = DataBindingUtil.setContentView<ActivityTextWritingBinding>(this, R.layout.activity_text_writing).apply {
            viewmodel = viewModel
        }

        setupNavigation()
    }

    private fun setupNavigation() {

        //뒤로 가기
        viewModel.exitClickEvent.observe(this, EventObserver {
            onBackPressed()
        })

        //작성하기
        viewModel.writeClickEvent.observe(this, EventObserver {
            onWriteClick()
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.exit -> onBackPressed()
        }
    }

    //작성하기
    fun onWriteClick() {

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

                insertTextmemo()

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

                updateTextmemo()
                setViewModel()

                val intent = Intent()
                intent.putExtra("result", viewModel.item.value)


                setResult(101, intent)


                finish()


            }
        }
    }

    fun setViewModel() {
        
        viewModel.item.value?.title = binding.title.text.toString()
        viewModel.item.value?.subTitle = binding.subTitle.text.toString()
        viewModel.item.value?.content = binding.content.text.toString()

    }

    fun insertTextmemo() {
        viewModel.insertTextMemoByRoom(
            binding.title.text!!.toString(),
            binding.subTitle.text!!.toString(),
            binding.content.text!!.toString()
        )
    }

    fun updateTextmemo() {
        viewModel.updateTextMemoByRoom(
            binding.title.text!!.toString(),
            binding.subTitle.text!!.toString(),
            binding.content.text!!.toString()
        )
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?")
            setPositiveButton("확인", dialogListener)
            setNegativeButton("취소", null)
        }.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }
}
