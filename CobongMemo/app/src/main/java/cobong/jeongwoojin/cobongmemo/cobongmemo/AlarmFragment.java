package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;


public class AlarmFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<AlarmListItem> list;
    MyAlarmAdapter myAlarmAdapter;

    // Chosen values
    SelectedDate mSelectedDate;
    int mHour, mMinute;
    String mRecurrenceOption, mRecurrenceRule;

    TextView tempText;

    //Database
    DBHelper helper;
    SQLiteDatabase db;


    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
            //rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            mSelectedDate = selectedDate;
            mHour = hourOfDay;
            mMinute = minute;
            mRecurrenceOption = recurrenceOption != null ?
                    recurrenceOption.name() : "n/a";
            mRecurrenceRule = recurrenceRule != null ?
                    recurrenceRule : "n/a";

            /*Toast.makeText(getContext(),"mSelectedDate : "+mSelectedDate +" mHour : "+mHour +" mMinute : "+mMinute + "mRecurrenceOption : "+ recurrenceOption +" mRecuurenceRule : "+recurrenceRule
            ,Toast.LENGTH_LONG).show();*/



            Toast.makeText(getContext()," mRecuurenceRule : "+recurrenceRule   ,Toast.LENGTH_LONG).show();
            tempText.setText("mSelectedDate : "+mSelectedDate +" mHour : "+mHour +" mMinute : "+mMinute + "mRecurrenceOption : "+ recurrenceOption +" mRecuurenceRule : "+recurrenceRule);

            DBHelper helper = new DBHelper(getContext());
            db = helper.getWritableDatabase();

            String insertAlarm;
            insertAlarm = "insert into alarm(hour,minute,recurOption,recurRule) values(?,?,?,?)";
            String args[] = {Integer.toString(mHour), Integer.toString(mMinute), mRecurrenceOption, mRecurrenceRule};
            db.execSQL(insertAlarm, args);
            //finish();

            Toast.makeText(getContext(), "디비 입력 완료", Toast.LENGTH_LONG).show();


            //새로고침
            onStart();

        }
    };


    public AlarmFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        //리스트 새로고침 해줘야됨
        loadList();

        myAlarmAdapter = new MyAlarmAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        recyclerView.setAdapter(myAlarmAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.fragment_alarm, container, false);
        tempText = (TextView)v.findViewById(R.id.tempText);
        fab = (FloatingActionButton) v.findViewById(R.id.addAlarm);
        recyclerView = (RecyclerView)v.findViewById(R.id.alarm_recycler);
        //스크롤시 fab 없애기
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //개천재임 나 ;;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == SCROLL_STATE_DRAGGING){
                    fab.hide();
                }else if(newState == SCROLL_STATE_IDLE){

                    Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.delay);
                    fab.setAnimation(anim);
                    fab.show();
                }
            }
            /*@Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }*/
        });

        loadList();


        //list = new ArrayList<>();

        //list.add(new AlarmItem(8,45,new boolean[7]));

        myAlarmAdapter = new MyAlarmAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        recyclerView.setAdapter(myAlarmAdapter);
        recyclerView.addItemDecoration(new MyAlarmItemDecoration());




        fab.setOnClickListener(this);



        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addAlarm:
                //알람 추가 부분
                //Toast.makeText(getContext(), "this is Alarm Set", Toast.LENGTH_SHORT).show();
               /* Intent intent = new Intent(getContext(),SetAlarm.class);
                startActivity(intent);*/

                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);



                // Options
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                if (!optionsPair.first) { // If options are not valid
                    Toast.makeText(getContext(), "No pickers activated",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getFragmentManager(), "SUBLIME_PICKER");



                break;
        }
    }

    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
        displayOptions |= SublimeOptions.ACTIVATE_RECURRENCE_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
        options.setPickerToShow(SublimeOptions.Picker.REPEAT_OPTION_PICKER);
        options.setDisplayOptions(displayOptions);

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    public void loadList(){


        list = new ArrayList<>();


        helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();

        Cursor cursor;
        String sel = "select * from alarm order by _id asc";
        try {
            cursor = db.rawQuery(sel, null);


            if(cursor != null) {
                while (cursor.moveToNext()) {
                    int index = cursor.getInt(0);
                    String temHour = cursor.getString(1);
                    String temMinute = cursor.getString(2);
                    String temrecurOption = cursor.getString(3);
                    String temrecurRule = cursor.getString(4);

                    list.add(new AlarmListItem(index, temHour, temMinute, temrecurOption, temrecurRule));
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyAlarmViewHolder extends RecyclerView.ViewHolder{

        TextView alarmText;

        public MyAlarmViewHolder(View itemView){
            super(itemView);

            alarmText = (TextView)itemView.findViewById(R.id.alarm_time);


        }

    }




    public class MyAlarmAdapter extends RecyclerView.Adapter<MyAlarmViewHolder>{




        private List<AlarmListItem> list;

        public MyAlarmAdapter(List<AlarmListItem> list){
            this.list = list;



        }


        @NonNull
        @Override
        public MyAlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item,parent,false);

            return new MyAlarmViewHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull MyAlarmViewHolder holder, final int position) {

            String hour = list.get(position).hour;
            String minute = list.get(position).minute;

            holder.alarmText.setText(hour+" : "+minute);


        }


        @Override
        public int getItemCount() {
            return list.size();
        }

    }


    class MyAlarmItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //int index = parent.getChildAdapterPosition(view)+1;

            outRect.set(30, 15, 30 ,15);


            view.setBackgroundColor(0xFFECE9E9);
            ViewCompat.setElevation(view, 30.0f);


        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);


            int width = parent.getWidth();
            int height = parent.getHeight();


        }
    }



}
