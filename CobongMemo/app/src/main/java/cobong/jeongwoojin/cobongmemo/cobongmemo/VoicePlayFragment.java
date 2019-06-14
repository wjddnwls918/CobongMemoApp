package cobong.jeongwoojin.cobongmemo.cobongmemo;


import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.DialogFragment;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.DBHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;


public class VoicePlayFragment extends DialogFragment implements ProgressGenerator.OnCompleteListener{

    private static final String TAG = "VoicePlay";



    ProgressGenerator progressGenerator ;
    ActionProcessButton btnPlay ;
    ActionProcessButton btnclose;
    ActionProcessButton btnPause;


    private RotateLoading rotateLoading;
    String inputDate;
    String filename;
    DBHelper helper;
    SQLiteDatabase db;

    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    MediaPlayer player;

    final private static File RECORDED_FILE = Environment.getExternalStorageDirectory();

    int length;

    @Override
    public void onComplete() {
        //Toast.makeText(getContext(), R.string.Loading_Complete, Toast.LENGTH_LONG).show();
    }

    public VoicePlayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_voice_play, container, false);

        inputDate = getArguments().getString("inputdate");

        //Toast.makeText(getContext(),"VoicePlay : "+inputDate,Toast.LENGTH_LONG).show();


        filename = RECORDED_FILE+"/"+this.inputDate +".mp3";



        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateloading);
        progressGenerator = new ProgressGenerator(this);


        btnclose = (ActionProcessButton) view.findViewById(R.id.playexit);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        // 오디오 플레이 버튼
        btnPlay = (ActionProcessButton) view.findViewById(R.id.play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                rotateLoading.start();
                btnPlay.setMode(ActionProcessButton.Mode.ENDLESS);
                progressGenerator.start(btnPlay);


                Toast.makeText(getContext(), filename, Toast.LENGTH_LONG).show();
                try {

                    // 오디오를 플레이 하기위해 MediaPlayer 객체 player를 생성한다.
                    player = new MediaPlayer ();
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            //Toast.makeText(getContext(), "재생이 완료됐습니다.",Toast.LENGTH_LONG).show();

                            rotateLoading.stop();
                            btnPlay.setProgress(100);
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
        });


        // 오디오 재생 중지 버튼
        btnPause =(ActionProcessButton)view.findViewById(R.id.pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (player == null)
                    return;

                rotateLoading.stop();
                btnPlay.setProgress(100);

                //Toast.makeText(getContext(), "재생이 중지되었습니다.", Toast.LENGTH_LONG).show();


                // 오디오 재생 중지
                player.stop();

                // 오디오 재생에 필요한 메모리들을 해제한다
                player.release();
                player = null;
            }
        });


        return view;
    }

    @Override
    public void onDestroy() {
        if( player != null){
            player.stop();
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player != null){
            //player.release();
            player.pause();
            length = player.getCurrentPosition();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(player != null){
            player.seekTo(length);
            player.start();
        }

    }
}



