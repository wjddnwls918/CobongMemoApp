package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VoiceRecordFragment extends DialogFragment implements ProgressGenerator.OnCompleteListener{


    ProgressGenerator progressGenerator ;
    ActionProcessButton btnRecord ;
    ActionProcessButton btnStop ;
    ActionProcessButton btnclose;
    private RotateLoading rotateLoading;

    DBHelper helper;
    SQLiteDatabase db;

    ImageView play;
    ImageView pause;

    String filename = "";
    // MediaPlayer 클래스에 재생에 관련된 메서드와 멤버변수가 저장어되있다.
    MediaPlayer player;
    // MediaRecorder 클래스에  녹음에 관련된 메서드와 멤버 변수가 저장되어있다.
    MediaRecorder recorder;
    final private static File RECORDED_FILE = Environment.getExternalStorageDirectory();
    String resultDate;

    private DialogInterface.OnDismissListener onDismissListener;
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener){
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onDismissListener != null){
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onComplete() {
        //Toast.makeText(getContext(), R.string.Loading_Complete, Toast.LENGTH_LONG).show();
    }

    public VoiceRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_voice_record, container, false);




        rotateLoading = (RotateLoading)view.findViewById(R.id.rotateloading);

        progressGenerator = new ProgressGenerator(this);

        btnRecord = (ActionProcessButton) view.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rotateLoading.start();
                btnRecord.setMode(ActionProcessButton.Mode.ENDLESS);
                progressGenerator.start(btnRecord);

                resultDate = curDate();
                filename =RECORDED_FILE.getAbsolutePath()+"/"+resultDate+".mp3";



                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setOutputFile(filename);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    recorder.prepare();
                }catch (Exception e){
                    e.printStackTrace();
                }
                recorder.start();


            }
        });

        btnStop = (ActionProcessButton)view.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateLoading.stop();
                btnRecord.setProgress(100);

                recorder.stop();
                recorder.release();
                recorder = null;

                writeDBvoice(resultDate);
            }
        });

        btnclose = (ActionProcessButton)view.findViewById(R.id.voiceexit);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onDestroy() {
        if(recorder != null)
        {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        super.onDestroy();
    }

    public String curDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String getTime = sdf.format(date);

        return getTime;
    }

    public void writeDBvoice(String resultDate){
        helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();

        String insertVoice;
        //Toast.makeText(getContext(),resultDate,Toast.LENGTH_SHORT).show();
        insertVoice = "insert into memo(title,memo_type,ID_VOICE) values(?,?,?)";
        String args[] = {resultDate, "voice", resultDate};
        try {
            db.execSQL(insertVoice,args);
            //Toast.makeText(getContext(),"입력 완료",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            //Toast.makeText(getContext(),"입력 실패",Toast.LENGTH_SHORT).show();
        }

    }

}
