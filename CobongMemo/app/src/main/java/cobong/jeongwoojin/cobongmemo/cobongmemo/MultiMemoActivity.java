package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MultiMemoActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView edit;
    ImageView remove;

    ImageView add;
    LinearLayout showContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        add = (ImageView)findViewById(R.id.addMemo);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.main_recycler);

        List<String> list = new ArrayList<>();
        for(int i=0; i<20; i++){
            list.add("Item="+i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager( this ));
        recyclerView.setAdapter(new MyAdapter(list));
        recyclerView.addItemDecoration(new MyItemDecoration());

        add.setOnClickListener(this);

    }

    //클릭 이벤트

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addMemo:
                Intent intent = new Intent(this,WriteMemoActivity.class);
                startActivity(intent);
                break;
        }
    }


    //메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //메뉴 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.cobong_custom:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;


        public MyViewHolder(View itemView){
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.memo_title);
            edit = (ImageView)itemView.findViewById(R.id.edit);
            remove = (ImageView)itemView.findViewById(R.id.remove);
            showContent = (LinearLayout)itemView.findViewById(R.id.showContent);


            //recyclerView 에서의 이벤트 처리는 ViewHolder에서 진행
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"edit 입니다.",Toast.LENGTH_SHORT).show();
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"remove 입니다.",Toast.LENGTH_SHORT).show();
                }
            });

            showContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"show Content 입니다.",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<String> list;
        public MyAdapter(List<String> list){
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
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String text = list.get(position);
            holder.title.setText(text);
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
            int index = parent.getChildAdapterPosition(view)+1;
            if( index % 3 == 0){
                outRect.set(20, 20, 20 ,40);

            }else{
                outRect.set(20, 20, 20, 20);
            }

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