package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.File;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentVoicePlayBinding;


public class VoicePlayFragment extends DialogFragment implements ProgressGenerator.OnCompleteListener, VoiceNavigator {

    private static final String TAG = "VoicePlay";

    private ProgressGenerator progressGenerator;

    private String inputDate;
    private String filename;

    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    private MediaPlayer player;

    final private static File RECORDED_FILE = Environment.getExternalStorageDirectory();

    private int length;

    private FragmentVoicePlayBinding binding;
    private VoiceViewModel viewModel;

    @Override
    public void onComplete() {

    }

    public VoicePlayFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voice_play, container, false);
        viewModel = ViewModelProviders.of(this).get(VoiceViewModel.class);
        binding.setViewmodel(viewModel);
        viewModel.setNavigator(this);

        inputDate = getArguments().getString("inputdate");
        filename = RECORDED_FILE + "/" + this.inputDate + ".mp3";

        progressGenerator = new ProgressGenerator(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            //player.release();
            player.pause();
            length = player.getCurrentPosition();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.seekTo(length);
            player.start();
        }

    }

    //시작
    @Override
    public void onPlayClick() {
        binding.rotateloading.start();
        binding.play.setMode(ActionProcessButton.Mode.ENDLESS);
        progressGenerator.start(binding.play);

        try {
            // 오디오를 플레이 하기위해 MediaPlayer 객체 player를 생성한다.
            player = new MediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //Toast.makeText(getContext(), "재생이 완료됐습니다.",Toast.LENGTH_LONG).show();

                    binding.rotateloading.stop();
                    binding.play.setProgress(100);
                    // 오디오 재생 중지
                    player.stop();

                    // 오디오 재생에 필요한 메모리들을 해제한다
                    player.release();
                    player = null;
                }
            });
            // 재생할 오디오 파일 저장위치를 설정
            player.setDataSource(filename);
            // 웹상에 있는 오디오 파일을 재생할때
            // player.setDataSource(Audio_Url);

            // 오디오 재생준비,시작
            player.prepare();
            player.start();
        } catch (Exception e) {
            Log.e("SampleAudioRecorder", "Audio play failed.", e);
        }
    }


    //정지
    @Override
    public void onPauseClick() {
        if (player == null)
            return;

        binding.rotateloading.stop();
        binding.play.setProgress(100);

        // 오디오 재생 중지
        player.stop();

        // 오디오 재생에 필요한 메모리들을 해제한다
        player.release();
        player = null;
    }

    //닫기
    @Override
    public void onExitClick() {
        dismiss();
    }

}



