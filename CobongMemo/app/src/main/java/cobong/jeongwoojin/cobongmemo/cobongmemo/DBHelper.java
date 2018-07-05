package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context){

        super(context, "memodb", null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String memoSQL = "create table memo_tb "+
                "(idx integer primary key autoincrement,"+
                "title not null,"+
                "subtitle,"+
                "content not null)";

        db.execSQL(memoSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table memo_tb");
            onCreate(db);
        }
    }
}
