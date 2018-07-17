package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    //
    public static String TABLE_MEMO = "MEMO";

    //table name for VOICE
    public static String TABLE_VOICE = "VOICE";

    //table name for HANDWRITING
    public static String TABLE_HANDWRITING = "HANDWRITING";


    public static final int DATABASE_VERSION = 1;


    public DBHelper(Context context){

        super(context, "memodb", null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        //메인 테이블
        String drop_sql = "drop table if exists "+TABLE_MEMO;

        try {
            db.execSQL(drop_sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        String memoSQL = "create table "+ TABLE_MEMO +
                "(idx integer not null primary key autoincrement,"+
                "  INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                "TITLE not null,"+
                "SUBTITLE TEXT DEFAULT '',"+
                "CONTENT TEXT DEFAULT '', " +
                " MEMO_TYPE text default '', "+
                " ID_VOICE INTEGER, "+
                " ID_HANDWRITING INTEGER "+
                ")";

        try {
            db.execSQL(memoSQL);
        }catch (Exception e){
            e.printStackTrace();
        }




        // 음성 테이블

        drop_sql = "drop table if exists " + TABLE_VOICE;
        try {
            db.execSQL(drop_sql);
        } catch(Exception e) {
            e.printStackTrace();
        }


        // create table
        String CREATE_SQL = "create table " + TABLE_VOICE + "("
                + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + "  URI TEXT, "
                + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                + ")";
        try {
            db.execSQL(CREATE_SQL);
        } catch(Exception e) {
            e.printStackTrace();
        }
        // create index
        String CREATE_INDEX_SQL = "create index " + TABLE_VOICE + "_IDX ON " + TABLE_VOICE + "("
                + "URI"
                + ")";
        try {
            db.execSQL(CREATE_INDEX_SQL);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //END OF SETTING VOICE TABLE


        // TABLE_HANDWRITING
        // drop existing table

        drop_sql = "drop table if exists " + TABLE_HANDWRITING;
        try {
            db.execSQL(drop_sql);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // create table
        CREATE_SQL = "create table " + TABLE_HANDWRITING + "("
                + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + "  URI TEXT, "
                + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                + ")";
        try {
            db.execSQL(CREATE_SQL);
        } catch(Exception e) {
            e.printStackTrace();
        }
        // create index
        CREATE_INDEX_SQL = "create index " + TABLE_HANDWRITING + "_IDX ON " + TABLE_HANDWRITING + "("
                + "URI"
                + ")";
        try {
            db.execSQL(CREATE_INDEX_SQL);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //END OF SETTING HANDWRITING TABLE


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table "+TABLE_MEMO);
            onCreate(db);
        }
    }
}
