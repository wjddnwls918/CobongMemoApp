package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;


public class AlarmFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<AlarmItem> list;
    MyAlarmAdapter myAlarmAdapter;

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
        /*loadList();

        myAdapter = new MyAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        recyclerView.setAdapter(myAdapter);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.fragment_alarm, container, false);
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

        list = new ArrayList<>();

        list.add(new AlarmItem(8,45,new boolean[7]));

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
                Toast.makeText(getContext(), "this is fab", Toast.LENGTH_SHORT).show();
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




        private List<AlarmItem> list;

        public MyAlarmAdapter(List<AlarmItem> list){
            this.list = list;



        }


        @NonNull
        @Override
        public MyAlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item,parent,false);

            return new MyAlarmViewHolder(view);
        }
/*

        @Override
        public WrittingAdapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);//뷰 생성(아이템 레이아웃을 기반으로)
            MyViewholder viewholder1 = new MyViewholder(view);//아이템레이아웃을 기반으로 생성된 뷰를 뷰홀더에 인자로 넣어줌


            return viewholder1;
        }
*/



        @Override
        public void onBindViewHolder(@NonNull MyAlarmViewHolder holder, final int position) {

            int hour = list.get(position).hour;
            int minute = list.get(position).minute;

            holder.alarmText.setText(hour+" : "+minute);

            /*String resultTitle = list.get(position).title;
            String resultSubTitle = list.get(position).subTitle;
            String resultMemoType = list.get(position).memo_type;
            String resultInputTime = list.get(position).input_time;


            if(resultMemoType.equals("voice")){
                holder.memoSubTitle.setVisibility(View.INVISIBLE);
            }


            holder.title.setText("제목 : "+resultTitle);
            holder.memoSubTitle.setText(resultSubTitle);
            holder.memoInputTime.setText(resultInputTime);

            if(resultMemoType.equals("text")){
                holder.text_type.setVisibility(View.VISIBLE);
            }else if(resultMemoType.equals("handwrite")){
                holder.handwriting_type.setVisibility(View.VISIBLE);
            }else{
                holder.voice_type.setVisibility(View.VISIBLE);
            }

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getApplicationContext(),"edit 입니다.",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),list.get(position).title,Toast.LENGTH_SHORT).show();

                    if(list.get(position).memo_type.equals("text")) {
                        Intent intent1 = new Intent(getActivity(), WriteMemoActivity.class);
                        intent1.putExtra("index", list.get(position).index);
                        intent1.putExtra("type", "editmemo");
                        intent1.putExtra("title", list.get(position).title);
                        intent1.putExtra("subtitle", list.get(position).subTitle);
                        intent1.putExtra("content", list.get(position).content);
                        startActivity(intent1);
                    }else if(list.get(position).memo_type.equals("handwrite")){
                        Intent intent = new Intent(getActivity(),HandWritingActivity.class);
                        intent.putExtra("handwriteId",list.get(position).handwriteId);
                        intent.putExtra("type","edit");
                        intent.putExtra("title",list.get(position).title);
                        intent.putExtra("subTitle",list.get(position).subTitle);
                        startActivity(intent);
                    }else{

                    }


                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),"remove 입니다.",Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("경고")
                            .setMessage("메모를 지우시겠습니까?")
                            .setPositiveButton("닫기",null)
                            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    int index = list.get(position).index;
                                    helper = new DBHelper(getContext());
                                    db = helper.getWritableDatabase();

                                    String del = "delete from memo where `idx`="+index;
                                    db.execSQL(del);


                                    //저장 파일 삭제
                                    File file;
                                    boolean deleted;
                                    if(list.get(position).memo_type.equals("voice")){
                                        file = new File(root+"/"+list.get(position).voiceId+".mp3");
                                        deleted = file.delete();
                                        //Toast.makeText(getContext(),"voice : "+Boolean.toString(deleted),Toast.LENGTH_LONG).show();
                                    }else if(list.get(position).memo_type.equals("handwrite")){
                                        file = new File(root+"/saved_images/"+list.get(position).handwriteId+".jpg");
                                        deleted = file.delete();
                                        //Toast.makeText(getContext(),"handwrite : "+Boolean.toString(deleted),Toast.LENGTH_LONG).show();
                                    }



                                    loadList();
                                    list.remove(position);
                                    recyclerView.removeViewAt(position);
                                    myAdapter.notifyItemRemoved(position);
                                    myAdapter.notifyItemRangeChanged(position,list.size());


                                     *//*recyclerView.setLayoutManager(new LinearLayoutManager( MultiMemoActivity.this ));
                                    recyclerView.setAdapter(new MyAdapter(list));
                                    recyclerView.addItemDecoration(new MyItemDecoration());*//*

                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();


                }
            });

            holder.showContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),"show Content 입니다.",Toast.LENGTH_SHORT).show();

                    if(list.get(position).memo_type.equals("text")) {
                        Intent intent = new Intent(getContext(), TextMemoView.class);

                        //Toast.makeText(getApplicationContext(),list.get(position).title,Toast.LENGTH_SHORT).show();

                        intent.putExtra("index", list.get(position).index);
                        intent.putExtra("title", list.get(position).title);
                        intent.putExtra("subtitle", list.get(position).subTitle);
                        intent.putExtra("content", list.get(position).content);
                        startActivity(intent);
                    }else if(list.get(position).memo_type.equals("voice")){
                        *//*DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                        int width = dm.widthPixels;
                        int height = dm.heightPixels;



                        VoicePlay dial = new VoicePlay(getContext(),list.get(position).voiceId);
                        //Toast.makeText(getContext(),"MemoListFragment : "+list.get(position).voiceId+ " title : "+list.get(position).title,Toast.LENGTH_SHORT).show();
                        WindowManager.LayoutParams wm = dial.getWindow().getAttributes();
                        wm.copyFrom(dial.getWindow().getAttributes());
                        wm.width = width;
                        wm.height = height/3;
                        dial.setCanceledOnTouchOutside(false);
                        dial.show();*//*


                        VoicePlayFragment dialogFragment = new VoicePlayFragment();
                        Bundle inputdate = new Bundle();
                        inputdate.putString("inputdate",list.get(position).voiceId);
                        dialogFragment.setArguments(inputdate);
                        dialogFragment.show(getActivity().getSupportFragmentManager(),"tag");

                    }else{
                        Intent intent = new Intent(getContext(), HandwriteViewActivity.class);
                        intent.putExtra("index",list.get(position).index);
                        intent.putExtra("title",list.get(position).title);
                        intent.putExtra("subtitle",list.get(position).subTitle);
                        intent.putExtra("handwriteId",list.get(position).handwriteId);
                        startActivity(intent);
                    }

                }
            });*/
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
/*

            Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.android, null);
            int drWidth = dr.getIntrinsicWidth();
            int drHeight = dr.getIntrinsicHeight();

            int left = width/2 - drWidth/2;
            int top = height/2 - drHeight/2;

            c.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.android),left, top, null);

*/

        }
    }


}
