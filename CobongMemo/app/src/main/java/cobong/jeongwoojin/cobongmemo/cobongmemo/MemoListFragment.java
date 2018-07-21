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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
                    list.add(new MyListItem(index, temTitle, temSubTitle, temcontent, temMemoType, temInputTime,temVoiceId));
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



        //set current locale
        Locale curLocale = getResources().getConfiguration().locale;
        BasicInfo.language = curLocale.getLanguage();
        //Log.d(TAG, "current language : " + BasicInfo.language);




        // SD Card checking
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getContext(), R.string.no_sdcard_message, Toast.LENGTH_LONG).show();
            return null;
        } else {
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!BasicInfo.ExternalChecked && externalPath != null) {
                BasicInfo.ExternalPath = externalPath + File.separator;
                Log.d(TAG, "ExternalPath : " + BasicInfo.ExternalPath);

                BasicInfo.FOLDER_PHOTO = BasicInfo.ExternalPath + BasicInfo.FOLDER_PHOTO;
                BasicInfo.FOLDER_VIDEO = BasicInfo.ExternalPath + BasicInfo.FOLDER_VIDEO;
                BasicInfo.FOLDER_VOICE = BasicInfo.ExternalPath + BasicInfo.FOLDER_VOICE;
                BasicInfo.FOLDER_HANDWRITING = BasicInfo.ExternalPath + BasicInfo.FOLDER_HANDWRITING;
                BasicInfo.DATABASE_NAME = BasicInfo.ExternalPath + BasicInfo.DATABASE_NAME;

                BasicInfo.ExternalChecked = true;
            }
        }


        loadList();


        myAdapter = new MyAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration());

        //권한
        checkDangerousPermissions();
        checkDangerousPermissions();

        fab.setOnClickListener(this);

       /* ImageView test = v.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });*/

        // Inflate the layout for this fragment
        return v;
    }

    //권한 부여 함수
    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.RECORD_AUDIO
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(getContext(), permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
                //Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }
    }
    //권한 허용, 비허용
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    //클릭 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addMemo:

                CharSequence memoType[] = new CharSequence[]{"글","음성"};
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

                                Toast.makeText(getContext(),"음성 메모",Toast.LENGTH_SHORT).show();
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
                                    deleteData(list.get(position).index);



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
                    }else{

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

    public void deleteData(int index){

        helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();

        String del = "delete from memo where `idx`="+index;
        db.execSQL(del);



    }



}
