package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dd.processbutton.iml.ActionProcessButton

import java.io.File

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentVoicePlayBinding


class VoicePlayFragment : DialogFragment(), ProgressGenerator.OnCompleteListener, VoiceNavigator {

    private var progressGenerator: ProgressGenerator? = null

    private var inputDate: String? = null
    private var filename: String? = null

    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    private var player: MediaPlayer? = null

    private var length: Int = 0

    private var binding: FragmentVoicePlayBinding? = null
    private var viewModel: VoiceViewModel? = null

    override fun onComplete() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voice_play, container, false)
        viewModel = ViewModelProviders.of(this).get(VoiceViewModel::class.java)
        binding!!.viewmodel = viewModel
        viewModel!!.setNavigator(this)

        inputDate = arguments!!.getString("inputdate")
        filename = RECORDED_FILE.toString() + "/" + this.inputDate + ".mp3"

        progressGenerator = ProgressGenerator(this)

        return binding!!.root
    }

    override fun onDestroy() {
        if (player != null) {
            player!!.stop()
            player!!.release()
            player = null
        }
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            //player.release();
            player!!.pause()
            length = player!!.currentPosition
        }

    }

    override fun onResume() {
        super.onResume()
        if (player != null) {
            player!!.seekTo(length)
            player!!.start()
        }

    }

    //시작
    override fun onPlayClick() {
        binding!!.rotateloading.start()
        binding!!.play.setMode(ActionProcessButton.Mode.ENDLESS)
        progressGenerator!!.start(binding!!.play)

        try {
            // 오디오를 플레이 하기위해 MediaPlayer 객체 player를 생성한다.
            player = MediaPlayer()
            player!!.setOnCompletionListener {
                //Toast.makeText(getContext(), "재생이 완료됐습니다.",Toast.LENGTH_LONG).show();

                binding!!.rotateloading.stop()
                binding!!.play.progress = 100
                // 오디오 재생 중지
                player!!.stop()

                // 오디오 재생에 필요한 메모리들을 해제한다
                player!!.release()
                player = null
            }
            // 재생할 오디오 파일 저장위치를 설정
            player!!.setDataSource(filename)
            // 웹상에 있는 오디오 파일을 재생할때
            // player.setDataSource(Audio_Url);

            // 오디오 재생준비,시작
            player!!.prepare()
            player!!.start()
        } catch (e: Exception) {
            Log.e("SampleAudioRecorder", "Audio play failed.", e)
        }

    }


    //정지
    override fun onPauseClick() {
        if (player == null)
            return

        binding!!.rotateloading.stop()
        binding!!.play.progress = 100

        // 오디오 재생 중지
        player!!.stop()

        // 오디오 재생에 필요한 메모리들을 해제한다
        player!!.release()
        player = null
    }

    //닫기
    override fun onExitClick() {
        dismiss()
    }

    companion object {

        private val RECORDED_FILE = Environment.getExternalStorageDirectory()
    }

}



