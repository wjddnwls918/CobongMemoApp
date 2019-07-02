package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication.Companion.root
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityHandwriteViewBinding
import java.io.File

class HandwriteViewActivity : AppCompatActivity(), HandwriteNavigator {

    private lateinit var binding: ActivityHandwriteViewBinding
    private lateinit var viewModel: HandwriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_handwrite_view)

        viewModel = ViewModelProviders.of(this).get(HandwriteViewModel::class.java)
        viewModel.navigator = this

        viewModel.item = intent.getParcelableExtra("handwriteItem")

        binding.viewmodel = viewModel

        val bitmap =
            BitmapFactory.decodeFile(root + "/saved_images/" + viewModel.item?.handwriteId + ".jpg")

        binding.handwriteViewImage.setImageBitmap(bitmap)

    }

    override fun onExitClick() {
        finish()
    }

    override fun onDeleteClick() {
        val builder = AlertDialog.Builder(this@HandwriteViewActivity)
        builder.setTitle("확인")
            .setMessage("메모를 지우시겠습니까?")
            .setNegativeButton("확인") { _, _ -> delHandwrite() }
            .setPositiveButton("취소", null)

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun delHandwrite() {

        viewModel.deleteHandwriteMemo(viewModel.item!!.index)

        val path = MemoApplication.root + "/saved_images/" + viewModel.item?.handwriteId + ".jpg"
        val file = File(path)
        Log.d("filedelete", path)
        if (file.exists()) {
            if (file.delete()) {
                Log.d("filedelete", "삭제완료")
            } else {
                Log.d("filedelete", "실패")
            }
        }
        finish()

    }
}
