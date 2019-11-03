package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo

import android.content.DialogInterface
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentVoiceRecordBinding
import com.dd.processbutton.iml.ActionProcessButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VoiceRecordFragment : DialogFragment(), ProgressGenerator.OnCompleteListener{

    private var progressGenerator: ProgressGenerator? = null

    private var filename = ""
    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    private var recorder: MediaRecorder? = null
    private lateinit var voiceTitle: String

    private var onDismissListener: DialogInterface.OnDismissListener? = null

    private lateinit var binding: FragmentVoiceRecordBinding
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: VoiceViewModel

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    override fun onDismiss(dialog: DialogInterface) {
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

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(VoiceViewModel::class.java).apply {
                item = arguments?.getParcelable("voiceItem")
            }
        binding = FragmentVoiceRecordBinding.inflate(inflater,container,false).apply {
            viewmodel = viewModel
        }

        progressGenerator = ProgressGenerator(this)

        setupNavigation()

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

    private fun setupNavigation() {

        //닫기
        viewModel.exitClickEvent.observe(this, EventObserver {
            dismiss()
        })

        //녹음 시작
        viewModel.recordClickEvent.observe(this, EventObserver {
            onRecordClick()
        })

        //녹음 끝
        viewModel.recordStopClickEvent.observe(this, EventObserver {
            onStopClick()
        })
    }

    //녹음 시작
    fun onRecordClick() {

        if (!binding.rlLoading.isStart) {
            binding.rlLoading.start()
            binding.apbRecord.setMode(ActionProcessButton.Mode.ENDLESS)
            progressGenerator!!.start(binding.apbRecord)

            if(binding.tietVoiceTitle.text!!.toString().equals("")) {
                voiceTitle = DateUtil.curDateForVoiceMemo()
            } else {
                voiceTitle = binding.tietVoiceTitle.text.toString()
            }
            filename = MemoApplication.root + "/" + voiceTitle + ".mp3"

            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(filename)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            }

            try {
                recorder!!.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            GlobalScope.launch {
                recorder!!.start()

            }
        }
    }

    //녹음 끝
    fun onStopClick() {
        binding.rlLoading.stop()
        binding.apbRecord.progress = 100

        if (recorder != null) {
            recorder!!.stop()
            recorder!!.release()
            recorder = null

            //insert voice memo
            viewModel.insertVoiceMemoByRoom(voiceTitle)
        }
    }


}
