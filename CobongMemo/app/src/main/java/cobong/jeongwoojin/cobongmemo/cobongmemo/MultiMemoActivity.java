package cobong.jeongwoojin.cobongmemo.cobongmemo;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MultiMemoActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView edit;
    ImageView remove;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edit = (ImageView)findViewById(R.id.edit);
        remove = (ImageView)findViewById(R.id.remove);

        fab = (FloatingActionButton)findViewById(R.id.cobong_fab);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.lab3_recycler);

        List<String> list = new ArrayList<>();
        for(int i=0; i<20; i++){
            list.add("Item="+i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager( this ));
        recyclerView.setAdapter(new MyAdapter(list));
        recyclerView.addItemDecoration(new MyItemDecoration());


        fab.setOnClickListener(this);
    }

    //클릭 이벤트
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cobong_fab:
                Toast.makeText(this,"메모 추가 버튼 입니다.",Toast.LENGTH_SHORT).show();


        }
    }

    //ImageView 이벤트 , xml파일에서 onClick 속성으로 이벤트에 사용할 함수를 등록한다.
    public void imageClick(View view){
        switch(view.getId()){
            case R.id.edit:
                Toast.makeText(this,"edit 입니다.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove:
                Toast.makeText(this,"remove 입니다.",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this,"환경설정 입니다.",Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;


        public MyViewHolder(View itemView){
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.memo_title);
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