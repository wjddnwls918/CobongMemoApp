package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo

import android.content.DialogInterface
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication.Companion.RECORDED_FILE
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentVoiceRecordBinding
import com.dd.processbutton.iml.ActionProcessButton


class VoiceRecordFragment : DialogFragment(), ProgressGenerator.OnCompleteListener, VoiceNavigator {

    private var progressGenerator: ProgressGenerator? = null

    private var filename = ""
    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    private var recorder: MediaRecorder? = null
    private lateinit var resultDate: String

    private var onDismissListener: DialogInterface.OnDismissListener? = null

    private lateinit var binding: FragmentVoiceRecordBinding
    private lateinit var viewModel: VoiceViewModel

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (onDismissListener != null) {
            onDismissListener!!.onDismiss(dialog)
        }
    }

    override fun onComplete() {
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_voice_record, container, false)

        viewModel = ViewModelProviders.of(this).get(VoiceViewModel::class.java)
        viewModel.navigator = this

        binding.viewmodel = viewModel

        progressGenerator = ProgressGenerator(this)

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onDestroy() {
        if (recorder != null) {
            recorder!!.stop()
            recorder!!.release()
            recorder = null
        }
        super.onDestroy()
    }


    //녹음 시작
    override fun onRecordClick() {
        binding.rotateloading.start()
        binding.btnRecord.setMode(ActionProcessButton.Mode.ENDLESS)
        progressGenerator!!.start(binding.btnRecord)

        resultDate = DateUtil.curDate()
        filename = RECORDED_FILE.absolutePath + "/" + resultDate + ".mp3"

        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setOutputFile(filename)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            recorder!!.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        recorder!!.start()
    }

    //녹음 끝
    override fun onStopClick() {
        binding.rotateloading.stop()
        binding.btnRecord.progress = 100

        recorder!!.stop()
        recorder!!.release()
        recorder = null

        //insert voice memo
        viewModel.insertVoice(resultDate)
    }

    //닫기
    override fun onExitClick() {
        dismiss()
    }

}
