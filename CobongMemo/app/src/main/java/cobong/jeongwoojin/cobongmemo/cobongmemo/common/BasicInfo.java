package cobong.jeongwoojin.cobongmemo.cobongmemo.common;


import android.os.Environment;

public class BasicInfo {

    public static String language = "";

    //
    public final static String root = Environment.getExternalStorageDirectory().toString();

    /**
     * 외장 메모리 패스
     */
    public static String ExternalPath = "/mnt/sdcard/";

    /**
     * 외장 메모리 패스 체크 여부
     */
    public static boolean ExternalChecked = false;

    /**
     * 사진 저장 위치
     */
    public static String FOLDER_PHOTO 		= "memo/photo/";

    /**
     * 동영상 저장 위치
     */
    public static String FOLDER_VIDEO 		= "memo/video/";

    /**
     * 녹음 저장 위치
     */
    public static String FOLDER_VOICE 		= "memo/voice/";

    /**
     * 손글씨 저장 위치
     */
    public static String FOLDER_HANDWRITING 	= "memo/handwriting/";

    /**
     * 미디어 포맷
     */
    public static final String URI_MEDIA_FORMAT		= "content://media";

    /**
     * 데이터베이스 이름
     */
    public static String DATABASE_NAME = "memo/memo.db";

}
