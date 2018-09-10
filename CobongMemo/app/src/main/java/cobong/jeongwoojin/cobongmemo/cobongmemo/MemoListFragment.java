package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MemoListFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "MultiMemoActivity";

    /**
     * table name for MEMO
     */
    public static String TABLE_MEMO = "MEMO";

    /**
     * table name for VOICE
     */
    public static String TABLE_VOICE = "VOICE";


    DBHelper helper;
    SQLiteDatabase db;

    TextView main_text;

    RecyclerView recyclerView;
    FloatingActionButton fab;

    List<MyListItem> list;

    MyAdapter myAdapter;

    final private static String root = Environment.getExternalStorageDirectory().toString();


    public MemoListFragment() {
        // Required empty public constructor
    }

    //load List or refresh List
    public void loadList(){


        list = new ArrayList<>();


        helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();

        Cursor cursor;
        String sel = "select * from memo order by idx desc";
        try {
            cursor = db.rawQuery(sel, null);


            if(cursor != null) {
                while (cursor.moveToNext()) {
                    int index = cursor.getInt(0);
                    String temTitle = cursor.getString(2);
                    String temSubTitle = cursor.getString(3);
                    String temcontent = cursor.getString(4);
                    String temMemoType = cursor.getString(5);
                    String temInputTime = cursor.getString(1);
                    String temVoiceId = cursor.getString(6);
                    String temHandwriteId = cursor.getString(7);
                    list.add(new MyListItem(index, temTitle, temSubTitle, temcontent, temMemoType, temInputTime,temVoiceId,temHandwriteId));
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();




    }

    @Override
    public void onStart() {
        super.onStart();

        loadList();

        myAdapter = new MyAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.fragment_memo_list, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.addMemo);
        recyclerView = (RecyclerView)v.findViewById(R.id.main_recycler);
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


        myAdapter = new MyAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration());




        fab.setOnClickListener(this);



        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    //클릭 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addMemo:

                CharSequence memoType[] = new CharSequence[]{"글","음성","손글씨"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("메모 타입 선택");
                builder.setItems(memoType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){

                            //글
                            case 0:
                                Intent intent = new Intent(getActivity(),WriteMemoActivity.class);
                                intent.putExtra("type","addmemo");
                                startActivity(intent);

                                break;

                            //음성
                            case 1:

                               // Toast.makeText(getContext(),"음성 메모",Toast.LENGTH_SHORT).show();
                                /*VoiceRecordFragment voicedialog = new VoiceRecordFragment();
                                voicedialog.show(getActivity().getSupportFragmentManager(),"tag");*/
                                /*DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                                int width = dm.widthPixels;
                                int height = dm.heightPixels;


                                VoiceRecord dial = new VoiceRecord(getContext());
                                WindowManager.LayoutParams wm = dial.getWindow().getAttributes();
                                wm.copyFrom(dial.getWindow().getAttributes());
                                wm.width = width;
                                wm.height = height/3;
                                dial.setCanceledOnTouchOutside(false);
                                dial.show();*/





                                VoiceRecordFragment dialogFragment = new VoiceRecordFragment();
                                dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        onStart();
                                    }
                                });
                                dialogFragment.show(getActivity().getSupportFragmentManager(),"tag");


                                //Toast.makeText(getContext(),"check out",Toast.LENGTH_LONG).show();





                                break;

                            case 2:
                                Intent intent3 = new Intent(getActivity(),HandWritingActivity.class);
                                intent3.putExtra("type","insert");
                                startActivity(intent3);
                                break;




                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();



                break;
        }



    }




    private class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView edit;
        public ImageView remove;
        public LinearLayout showContent;

        public TextView title;
        public TextView memoSubTitle;
        public TextView memoInputTime;

        public ImageView text_type;
        public ImageView voice_type;
        public ImageView handwriting_type;

        public MyViewHolder(View itemView){
            super(itemView);

            edit = (ImageView)itemView.findViewById(R.id.edit);
            remove = (ImageView)itemView.findViewById(R.id.remove);
            showContent = (LinearLayout)itemView.findViewById(R.id.showContent);

            title = (TextView)itemView.findViewById(R.id.memo_title);
            memoSubTitle = (TextView)itemView.findViewById(R.id.memo_subtitle);
            memoInputTime = (TextView)itemView.findViewById(R.id.memo_inputTime);

            text_type = (ImageView)itemView.findViewById(R.id.text_type);
            voice_type = (ImageView)itemView.findViewById(R.id.voice_type);
            handwriting_type = (ImageView)itemView.findViewById(R.id.handwrite_type);


        }

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{



        private List<MyListItem> list;

        public MyAdapter(List<MyListItem> list){
            this.list = list;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item,parent,false);

            return new MyViewHolder(view);
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
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            String resultTitle = list.get(position).title;
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


                                     /*recyclerView.setLayoutManager(new LinearLayoutManager( MultiMemoActivity.this ));
                                    recyclerView.setAdapter(new MyAdapter(list));
                                    recyclerView.addItemDecoration(new MyItemDecoration());*/

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
                        /*DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                        int width = dm.widthPixels;
                        int height = dm.heightPixels;



                        VoicePlay dial = new VoicePlay(getContext(),list.get(position).voiceId);
                        //Toast.makeText(getContext(),"MemoListFragment : "+list.get(position).voiceId+ " title : "+list.get(position).title,Toast.LENGTH_SHORT).show();
                        WindowManager.LayoutParams wm = dial.getWindow().getAttributes();
                        wm.copyFrom(dial.getWindow().getAttributes());
                        wm.width = width;
                        wm.height = height/3;
                        dial.setCanceledOnTouchOutside(false);
                        dial.show();*/


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
            });
        }




        @Override
        public int getItemCount() {
            return list.size();
        }

    }


    class MyItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //int index = parent.getChildAdapterPosition(view)+1;

            outRect.set(30, 15, 30 ,15);


            view.setBackgroundColor(0xFFECE9E9);
            ViewCompat.setElevation(view, 20.0f);


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
