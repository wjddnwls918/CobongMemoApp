package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication.Companion.root
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityHandwriteViewBinding

class HandwriteViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHandwriteViewBinding
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: HandwriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(HandwriteViewModel::class.java).apply {
                item = intent.getParcelableExtra("handwriteItem")
            }

        binding = DataBindingUtil.setContentView<ActivityHandwriteViewBinding>(
            this,
            R.layout.activity_handwrite_view
        ).apply {
            viewmodel = viewModel
        }

        initViewImage()
        setupNavigation()
    }

    private fun initViewImage() {
        val bitmap =
            BitmapFactory.decodeFile(root + "/saved_images/" + viewModel.item.handwriteId + ".jpg")
        binding.handwriteViewImage.setImageBitmap(bitmap)
    }

    private fun setupNavigation() {

        //뒤로 가기
        viewModel.exitClickEvent.observe(this, EventObserver {
            finish()
        })

        //작성하기
        viewModel.deleteMemoEvent.observe(this, EventObserver {
            onDeleteClick()
        })
    }

    fun onDeleteClick() {
        AlertDialog.Builder(this@HandwriteViewActivity).apply {
            setTitle("확인")
                setMessage("메모를 지우시겠습니까?")
                setNegativeButton("확인") { _, _ -> delHandwrite() }
                setPositiveButton("취소", null)

        }.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    fun delHandwrite() {

        viewModel.deleteHandwriteMemoByRoom()
        viewModel.deleteHandwriteInStorage()
        finish()

    }
}
