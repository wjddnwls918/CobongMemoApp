package cobong.jeongwoojin.cobongmemo.cobongmemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentMemoListBinding;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.DBHelper;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.MemoAdapter;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.MemoNavigator;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.MemoViewModel;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo.HandwriteViewActivity;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo.HandwritingActivity;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo.TextMemoViewActivity;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo.TextMemoWriteActivity;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo.VoiceRecordFragment;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MemoListFragment extends Fragment implements View.OnClickListener, MemoNavigator {

    //TAG
    public static final String TAG = "MultiMemoActivity";
    public static String TABLE_MEMO = "MEMO";
    public static String TABLE_VOICE = "VOICE";

    //DB
    private DBHelper helper;
    private SQLiteDatabase db;

    //private List<MemoListItem> list;

    final private static String root = Environment.getExternalStorageDirectory().toString();

    private FragmentMemoListBinding binding;

    private MemoViewModel viewModel;
    private MemoAdapter memoAdapter;
    //MemoDatabase database = MemoDatabase.Companion.getDatabase(getContext().getApplicationContext());

    public MemoListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        //load data
        loadList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo_list, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(MemoViewModel.class);
        viewModel.setNavigator(this);

        //RecyclerView init
        initRecyclerView();

        //스크롤시 fab 없애기
        binding.rcvMemoList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //개천재임 나 ;;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == SCROLL_STATE_DRAGGING) {
                    binding.fabAdd.hide();
                } else if (newState == SCROLL_STATE_IDLE) {

                    Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.delay);
                    binding.fabAdd.setAnimation(anim);
                    binding.fabAdd.show();
                }
            }
        });


        binding.fabAdd.setOnClickListener(this);


        return binding.getRoot();
    }

    public void initRecyclerView() {
        memoAdapter = new MemoAdapter(new ArrayList<>(), viewModel);
        binding.rcvMemoList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMemoList.setAdapter(memoAdapter);
    }


    //load List or refresh List
    public void loadList() {


        List list = new ArrayList<>();


        helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();

        //list = database.memolistDao().getAllMemos();

        Cursor cursor;
        String sel = "select * from memo order by idx desc";
        try {
            cursor = db.rawQuery(sel, null);

            Log.d("dbarrive", "hello");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int index = cursor.getInt(0);
                    String temTitle = cursor.getString(2);
                    String temSubTitle = cursor.getString(3);
                    String temcontent = cursor.getString(4);
                    String temMemoType = cursor.getString(5);
                    String temInputTime = cursor.getString(1);
                    String temVoiceId = cursor.getString(6);
                    String temHandwriteId = cursor.getString(7);
                    list.add(new MemoListItem(index, temTitle, temSubTitle, temMemoType, temInputTime, temcontent, temVoiceId, temHandwriteId));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        memoAdapter.setItem(list);

    }

    //클릭 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:

                CharSequence memoType[] = new CharSequence[]{"글", "음성", "손글씨"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("메모 타입 선택");
                builder.setItems(memoType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            //글
                            case 0:
                                Intent intent = new Intent(getActivity(), TextMemoWriteActivity.class);
                                intent.putExtra("type", "addmemo");
                                startActivity(intent);

                                break;

                            //음성
                            case 1:

                                VoiceRecordFragment dialogFragment = new VoiceRecordFragment();
                                //Fragment 종료시 화면 새로고침
                                dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        onStart();
                                    }
                                });
                                dialogFragment.show(getActivity().getSupportFragmentManager(), "tag");

                                break;

                            case 2:
                                Intent intent3 = new Intent(getActivity(), HandwritingActivity.class);
                                intent3.putExtra("type", "insert");
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

    //메모 보기로 이동
    @Override
    public void sendMemo(MemoListItem item) {

        Intent intent;

        if (item.getMemoType().equals("text")) {
            intent = new Intent(getContext(), TextMemoViewActivity.class);
            intent.putExtra("textItem", item);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (item.getMemoType().equals("voice")) {

            VoicePlayFragment dialogFragment = new VoicePlayFragment();
            Bundle inputdate = new Bundle();
            inputdate.putString("inputdate", item.getVoiceId());
            dialogFragment.setArguments(inputdate);
            dialogFragment.show(getActivity().getSupportFragmentManager(), "tag");

        } else {
            intent = new Intent(getContext(), HandwriteViewActivity.class);
          /*  intent.putExtra("index", item.getIndex());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("subtitle", item.getSubTitle());
            intent.putExtra("handwriteId", item.getHandwriteId());*/
            intent.putExtra("handwriteItem", item);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }


    }

    @Override
    public void deleteMemo(MemoListItem memo) {
        Log.d("check arrive", "hihi " + memo.getIndex());


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("경고")
                .setMessage("메모를 지우시겠습니까?")
                .setPositiveButton("닫기", null)
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        helper = new DBHelper(getContext());
                        db = helper.getWritableDatabase();

                        String del = "delete from memo where `idx`=" + memo.getIndex();
                        db.execSQL(del);


                        //저장 파일 삭제
                        File file;
                        boolean deleted;

                        int curRealPos = memoAdapter.getItemPosition(memo);

                        if (memoAdapter.getItem(curRealPos).getMemoType().equals("voice")) {
                            file = new File(root + "/" + memoAdapter.getItem(curRealPos).getVoiceId() + ".mp3");
                            deleted = file.delete();

                        } else if (memoAdapter.getItem(curRealPos).getMemoType().equals("handwrite")) {
                            file = new File(root + "/saved_images/" + memoAdapter.getItem(curRealPos).getHandwriteId() + ".jpg");
                            deleted = file.delete();

                        }

                        memoAdapter.removeItem(memo);


                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    @Override
    public void editMemo(MemoListItem memo) {

        Intent intent;

        if (memo.getMemoType().equals("text")) {
            intent = new Intent(getActivity(), TextMemoWriteActivity.class);
            intent.putExtra("textItem", memo);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (memo.getMemoType().equals("handwrite")) {
            intent = new Intent(getActivity(), HandwritingActivity.class);
            intent.putExtra("handwriteItem", memo);
            intent.putExtra("type", "edit");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            Snackbar.make(getView(), "수정할 수 없습니다", Snackbar.LENGTH_SHORT).show();
        }
    }

    /*

    public class MemoAdapter extends BaseRecyclerViewAdapter<MemoListItem,MemoAdapter.MemoViewHolder> {

        //private List<MemoListItem> dataSet;

        public MemoAdapter(List<MemoListItem> list) {
            super(list);
            //this.dataSet = list;
        }


        @NonNull
        @Override
        public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            MemoItemBinding binding = MemoItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                    parent, false);

            return new MemoViewHolder(binding);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public void onBindView(MemoViewHolder holder, int position) {
            holder.binding.setMemo(getItem(position));
        }


        @Override
        public void onBindViewHolder(@NonNull MemoViewHolder holder, final int position) {


             String resultTitle = dataSet.get(position).getTitle();
            String resultSubTitle = dataSet.get(position).getSubTitle();
            String resultMemoType = dataSet.get(position).getMemoType();
            String resultInputTime = dataSet.get(position).getInputTime();


            if (resultMemoType.equals("voice")) {
                holder.binding.memoSubtitle.setVisibility(View.INVISIBLE);
            }


            holder.binding.memoTitle.setText("제목 : " + resultTitle);
            holder.binding.memoSubtitle.setText(resultSubTitle);
            holder.binding.memoInputTime.setText(resultInputTime);

            if (resultMemoType.equals("text")) {
                holder.binding.memoType.setImageDrawable(getResources().getDrawable(R.drawable.text));
            } else if (resultMemoType.equals("handwrite")) {

                holder.binding.memoType.setImageDrawable(getResources().getDrawable(R.drawable.handwriting));
            } else {
                holder.binding.memoType.setImageDrawable(getResources().getDrawable(R.drawable.voice));
            }



             holder.binding.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getApplicationContext(),"edit 입니다.",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),list.get(position).title,Toast.LENGTH_SHORT).show();

                    if (dataSet.get(position).getMemoType().equals("text")) {
                        Intent intent1 = new Intent(getActivity(), TextMemoWriteActivity.class);
                        intent1.putExtra("index", dataSet.get(position).getIndex());
                        intent1.putExtra("type", "editmemo");
                        intent1.putExtra("title", dataSet.get(position).getTitle());
                        intent1.putExtra("subtitle", dataSet.get(position).getSubTitle());
                        intent1.putExtra("content", dataSet.get(position).getContent());
                        startActivity(intent1);
                    } else if (dataSet.get(position).getMemoType().equals("handwrite")) {
                        Intent intent = new Intent(getActivity(), HandwritingActivity.class);
                        intent.putExtra("handwriteId", dataSet.get(position).getHandwriteId());
                        intent.putExtra("type", "edit");
                        intent.putExtra("title", dataSet.get(position).getTitle());
                        intent.putExtra("subTitle", dataSet.get(position).getSubTitle());
                        startActivity(intent);
                    } else {
                        Snackbar.make(getView(), "수정할 수 없습니다", Snackbar.LENGTH_SHORT).show();
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
                                    memoAdapter.notifyItemRemoved(position);
                                    memoAdapter.notifyItemRangeChanged(position,list.size());


                                     */
/*recyclerView.setLayoutManager(new LinearLayoutManager( MultiMemoActivity.this ));
                                    recyclerView.setAdapter(new memoAdapter(list));
                                    recyclerView.addItemDecoration(new MyItemDecoration());*//*


                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();


                }
            });


            holder.binding.showContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),"show Content 입니다.",Toast.LENGTH_SHORT).show();

                    if (dataSet.get(position).getMemoType().equals("text")) {
                        Intent intent = new Intent(getContext(), TextMemoViewActivity.class);

                        //Toast.makeText(getApplicationContext(),list.get(position).title,Toast.LENGTH_SHORT).show();

                        intent.putExtra("index", dataSet.get(position).getIndex());
                        intent.putExtra("title", dataSet.get(position).getTitle());
                        intent.putExtra("subtitle", dataSet.get(position).getSubTitle());
                        intent.putExtra("content", dataSet.get(position).getContent());
                        startActivity(intent);
                    } else if (dataSet.get(position).getMemoType().equals("voice")) {


                        VoicePlayFragment dialogFragment = new VoicePlayFragment();
                        Bundle inputdate = new Bundle();
                        inputdate.putString("inputdate", dataSet.get(position).getVoiceId());
                        dialogFragment.setArguments(inputdate);
                        dialogFragment.show(getActivity().getSupportFragmentManager(), "tag");

                    } else {
                        Intent intent = new Intent(getContext(), HandwriteViewActivity.class);
                        intent.putExtra("index", dataSet.get(position).getIndex());
                        intent.putExtra("title", dataSet.get(position).getTitle());
                        intent.putExtra("subtitle", dataSet.get(position).getSubTitle());
                        intent.putExtra("handwriteId", dataSet.get(position).getHandwriteId());
                        startActivity(intent);
                    }

                }
            });

        }




        class MemoViewHolder extends RecyclerView.ViewHolder {

            MemoItemBinding binding;

            public MemoViewHolder(MemoItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

        }

    }

*/

}
