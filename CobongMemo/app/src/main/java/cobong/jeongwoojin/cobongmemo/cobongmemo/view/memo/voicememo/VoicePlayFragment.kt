package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo


import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentVoicePlayBinding
import com.dd.processbutton.iml.ActionProcessButton


class VoicePlayFragment : DialogFragment(), ProgressGenerator.OnCompleteListener {

    private var progressGenerator: ProgressGenerator? = null

    //private var inputDate: String? = null
    private var filename: String? = null

    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    private var player: MediaPlayer? = null

    private var length: Int = 0

    private lateinit var binding: FragmentVoicePlayBinding
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: VoiceViewModel

    override fun onComplete() {
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


        binding = FragmentVoicePlayBinding.inflate(inflater,container,false).apply {
            viewmodel = viewModel
        }

        filename = MemoApplication.root +  "/" + viewModel.item?.title + ".mp3"
        Log.d("checkfilename",filename)

        progressGenerator = ProgressGenerator(this)

        setupNavigation()

        return binding.root
    }

    override fun onDestroy() {
        if (player != null) {
            player?.stop()
            player?.release()
            player = null
        }
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            //player.release();
            player?.pause()
            length = player!!.currentPosition
        }

    }

    override fun onResume() {
        super.onResume()
        if (player != null) {
            player?.seekTo(length)
            player?.start()
        }

    }

    private fun setupNavigation() {

        //닫기
        viewModel.exitClickEvent.observe(this, EventObserver {
            dismiss()
        })

        //녹음 시작
        viewModel.playClickEvent.observe(this, EventObserver {
            onPlayClick()
        })

        //녹음 끝
        viewModel.pauseClickEvent.observe(this, EventObserver {
            onPauseClick()
        })
    }

    //시작
    fun onPlayClick() {
        binding.rotateloading.start()
        binding.play.setMode(ActionProcessButton.Mode.ENDLESS)
        progressGenerator?.start(binding.play)

        try {
            // 오디오를 플레이 하기위해 MediaPlayer 객체 player를 생성한다.
            player = MediaPlayer()
            player?.setOnCompletionListener {

                binding.rotateloading.stop()
                binding.play.progress = 100
                // 오디오 재생 중지
                player?.stop()

                // 오디오 재생에 필요한 메모리들을 해제한다
                player?.release()
                player = null
            }
            // 재생할 오디오 파일 저장위치를 설정
            player?.setDataSource(filename)
            Log.d("checkdata",filename)
            // 오디오 재생준비,시작
            player?.prepare()
            player?.start()
        } catch (e: Exception) {
            Log.e("SampleAudioRecorder", "Audio play failed.", e)
        }

    }


    //정지
    fun onPauseClick() {
        if (player == null)
            return

        binding.rotateloading.stop()
        binding.play.progress = 100

        // 오디오 재생 중지
        player?.stop()

        // 오디오 재생에 필요한 메모리들을 해제한다
        player?.release()
        player = null
    }

}



