package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.File;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentVoiceRecordBinding;


public class VoiceRecordFragment extends DialogFragment implements ProgressGenerator.OnCompleteListener, VoiceNavigator {


    private ProgressGenerator progressGenerator;

    private String filename = "";
    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    private MediaPlayer player;
    // MediaRecorder 클래스에  녹음에 관련된 메서드와 멤버 변수가 저장되어있다.
    private MediaRecorder recorder;
    final private static File RECORDED_FILE = Environment.getExternalStorageDirectory();
    private String resultDate;

    private DialogInterface.OnDismissListener onDismissListener;

    private FragmentVoiceRecordBinding binding;

    private VoiceViewModel viewModel;

    public VoiceRecordFragment() {
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onComplete() {

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voice_record, container, false);

        viewModel = ViewModelProviders.of(this).get(VoiceViewModel.class);
        viewModel.setNavigator(this);

        binding.setViewmodel(viewModel);

        progressGenerator = new ProgressGenerator(this);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        super.onDestroy();
    }


    //녹음 시작
    @Override
    public void onRecordClick() {
        binding.rotateloading.start();
        binding.btnRecord.setMode(ActionProcessButton.Mode.ENDLESS);
        progressGenerator.start(binding.btnRecord);

        resultDate = DateUtil.curDate();
        filename = RECORDED_FILE.getAbsolutePath() + "/" + resultDate + ".mp3";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filename);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    //녹음 끝
    @Override
    public void onStopClick() {
        binding.rotateloading.stop();
        binding.btnRecord.setProgress(100);

        recorder.stop();
        recorder.release();
        recorder = null;

        //insert voice memo
        viewModel.insertVoice(resultDate);
    }

    //닫기
    @Override
    public void onExitClick() {
        dismiss();
    }
}
