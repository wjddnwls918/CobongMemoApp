package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityHandWritingBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import me.panavtec.drawableview.DrawableViewConfig
import java.io.File
import java.io.FileOutputStream

class HandwritingActivity : AppCompatActivity(), View.OnClickListener, ColorPickerDialogListener {

    private var config: DrawableViewConfig? = null

    private lateinit var handwriteId: String
    private var erase: Boolean = false
    private var temcol: Int = 0
    private var type: String? = null

    private var width: Int = 0
    private var height: Int = 0

    internal var dialogListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ -> finish() }

    private lateinit var binding: ActivityHandWritingBinding

    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: HandwriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(HandwriteViewModel::class.java)

        binding = DataBindingUtil.setContentView<ActivityHandWritingBinding>(
            this,
            R.layout.activity_hand_writing
        ).apply {
            viewmodel = viewModel
        }

        erase = false

        //캔버스 초기화
        initCanvas()

        //작성인지 수정인지 설정
        type = intent.getStringExtra("type")

        //읽어와서 저장
        if (intent.getParcelableExtra<MemoItem>("handwriteItem") != null) {
            viewModel.item = intent.getParcelableExtra("handwriteItem")

            val bitmap =
                BitmapFactory.decodeFile(MemoApplication.root + "/saved_images/" + viewModel.item.handwriteId + ".jpg")
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.paintView.background = bitmapDrawable
        }

        //클릭리스너 등록
        initClickListener()

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

    fun initCanvas() {
        val dm = resources.displayMetrics

        windowManager.defaultDisplay.getMetrics(dm)
        width = dm.widthPixels
        height = (dm.heightPixels * 0.58).toInt()

        config = DrawableViewConfig()
        config!!.strokeColor = ContextCompat.getColor(this, android.R.color.black)
        config!!.isShowCanvasBounds =
            true // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config!!.strokeWidth = 20.0f
        config!!.minZoom = 1.0f
        config!!.maxZoom = 3.0f

        config!!.canvasHeight = height
        config!!.canvasWidth = width

        binding.paintView.setConfig(config!!)
    }

    fun initClickListener() {
        binding.handwriteColor.setOnClickListener(this)
        binding.widthUp.setOnClickListener(this)
        binding.widthDown.setOnClickListener(this)
        binding.eraser.setOnClickListener(this)
        binding.undo.setOnClickListener(this)
        binding.blackPencil.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.handwriteColor ->
                //choice color
                ColorPickerDialog.newBuilder()
                    .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                    .setAllowPresets(false)
                    .setDialogId(0)
                    .setColor(Color.BLACK)
                    .setShowAlphaSlider(true)
                    .show(this)

            R.id.widthUp -> {
                config!!.strokeWidth = config!!.strokeWidth + 10
                val trans = (config!!.strokeWidth / 10 + 1).toInt().toString()
                viewModel.pencilSize.set(trans)
            }

            R.id.widthDown -> {

                if (config!!.strokeWidth - 10 >= 0) {
                    config!!.strokeWidth = config!!.strokeWidth - 10

                    val trans = (config!!.strokeWidth / 10 + 1).toInt().toString()
                    viewModel.pencilSize.set(trans)
                }

            }

            R.id.eraser -> {

                temcol = config!!.strokeColor
                config!!.strokeColor = Color.argb(255, 255, 255, 255)
                config!!.strokeWidth = 40.0f
            }

            R.id.undo -> binding.paintView.undo()


            R.id.blackPencil -> config!!.strokeColor = Color.argb(255, 0, 0, 0)
        }
    }

    override fun onColorSelected(dialogId: Int, color: Int) {

        temcol = color
        config!!.strokeColor = temcol

    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    //수정시
    fun editHandwrite() {

        val editFile =
            File(MemoApplication.root + "/saved_images/" + viewModel.item.handwriteId + ".jpg")
        try {
            val out: FileOutputStream
            val back = (binding.paintView.background.current as BitmapDrawable).bitmap
            val image = binding.paintView.obtainBitmap()


            //merge bitmap
            val result = Bitmap.createBitmap(back.width, back.height, back.config)
            val canvas2 = Canvas(result)
            val widthBack = back.width
            val widthFront = image.width
            val move = ((widthBack - widthFront) / 2).toFloat()
            canvas2.drawBitmap(back, 0f, 0f, null)
            canvas2.drawBitmap(image, move, move, null)


            out = FileOutputStream(editFile, false)
            result.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        //update handwrtie memo
        viewModel.updateHandWriteMemoByRoom(
            binding.handwriteTitle.text.toString(),
            binding.handwriteSubtitle.text.toString()
        )

    }

    //처음 작성 시
    fun insertHandWrite() {

        val myDir = File(MemoApplication.root + "/saved_images")
        myDir.mkdir()
        handwriteId = DateUtil.curDate()
        val fname = handwriteId + ".jpg"


        var out: FileOutputStream? = null
        try {

            val image = binding.paintView.obtainBitmap()

            val canvas = Canvas(image)
            canvas.drawColor(Color.WHITE)
            canvas.drawBitmap(binding.paintView.obtainBitmap(), 0f, 0f, null)

            out = FileOutputStream(File(myDir, fname), true)
            image.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //insert handwrtie memo
        viewModel.insertHandWriteMemoByRoom(
            binding.handwriteTitle.text!!.toString(),
            binding.handwriteSubtitle.text!!.toString(),
            handwriteId
        )

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


    //작성
    fun onWriteClick() {

        KeyBoardUtil.hideSoftKeyboard(binding.root, this)

        if (binding.handwriteTitle.text!!.toString().trim { it <= ' ' } == "" && binding.handwriteSubtitle.text!!.toString().trim { it <= ' ' } == "") {
            SnackBarUtil.showSnackBar(binding.root, R.string.text_input_title_subTitle)
        } else if (binding.handwriteTitle.text!!.toString().trim { it <= ' ' } == "") {
            SnackBarUtil.showSnackBar(binding.root, R.string.text_input_title)
        } else if (binding.handwriteSubtitle.text!!.toString().trim { it <= ' ' } == "") {
            SnackBarUtil.showSnackBar(binding.root, R.string.text_input_subTitle)
        } else {

            if (type != "insert") {
                editHandwrite()
            } else {
                insertHandWrite()

            }
            finish()
        }
    }

}
