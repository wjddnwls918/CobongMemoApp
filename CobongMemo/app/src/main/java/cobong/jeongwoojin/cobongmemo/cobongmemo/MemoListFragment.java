package cobong.jeongwoojin.cobongmemo.cobongmemo

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import java.util.ArrayList

import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentMemoListBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.MemoItemBinding

import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE

class MemoListFragment : Fragment(), View.OnClickListener {

    //DB
    private var helper: DBHelper? = null
    private var db: SQLiteDatabase? = null

    private var list: MutableList<MyListItem>? = null
    private var myAdapter: MemoAdapter? = null

    private var binding: FragmentMemoListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo_list, container, false)

        //스크롤시 fab 없애기
        binding!!.rcvMemoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            //개천재임 나 ;;
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == SCROLL_STATE_DRAGGING) {
                    binding!!.fabAdd.hide()
                } else if (newState == SCROLL_STATE_IDLE) {

                    val anim = AnimationUtils.loadAnimation(context, R.anim.delay)
                    binding!!.fabAdd.animation = anim
                    binding!!.fabAdd.show()
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
        })


        loadList()

        initRecyclerView()


        binding!!.fabAdd.setOnClickListener(this)


        // Inflate the layout for this fragment
        return binding!!.root
    }

    fun initRecyclerView() {
        myAdapter = MemoAdapter(list)
        binding!!.rcvMemoList.layoutManager = LinearLayoutManager(context)
        binding!!.rcvMemoList.adapter = myAdapter
    }


    //load List or refresh List
    fun loadList() {


        list = ArrayList()


        helper = DBHelper(context)
        db = helper!!.writableDatabase

        val cursor: Cursor?
        val sel = "select * from memo order by idx desc"
        try {
            cursor = db!!.rawQuery(sel, null)


            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val index = cursor.getInt(0)
                    val temTitle = cursor.getString(2)
                    val temSubTitle = cursor.getString(3)
                    val temcontent = cursor.getString(4)
                    val temMemoType = cursor.getString(5)
                    val temInputTime = cursor.getString(1)
                    val temVoiceId = cursor.getString(6)
                    val temHandwriteId = cursor.getString(7)
                    list!!.add(
                        MyListItem(
                            index,
                            temTitle,
                            temSubTitle,
                            temcontent,
                            temMemoType,
                            temInputTime,
                            temVoiceId,
                            temHandwriteId
                        )
                    )
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()


    }

    override fun onStart() {
        super.onStart()

        loadList()

        initRecyclerView()

    }


    override fun onDestroy() {

        super.onDestroy()
    }

    //클릭 이벤트
    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_add -> {

                val memoType = arrayOf<CharSequence>("글", "음성", "손글씨")
                val builder = AlertDialog.Builder(context)
                builder.setTitle("메모 타입 선택")
                builder.setItems(memoType) { dialog, which ->
                    when (which) {

                        //글
                        0 -> {
                            val intent = Intent(activity, WriteMemoActivity::class.java)
                            intent.putExtra("type", "addmemo")
                            startActivity(intent)
                        }

                        //음성
                        1 -> {

                            val dialogFragment = VoiceRecordFragment()
                            //Fragment 종료시 화면 새로고침
                            dialogFragment.setOnDismissListener { onStart() }
                            dialogFragment.show(activity!!.supportFragmentManager, "tag")
                        }

                        2 -> {
                            val intent3 = Intent(activity, HandWritingActivity::class.java)
                            intent3.putExtra("type", "insert")
                            startActivity(intent3)
                        }
                    }
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
        }


    }


    inner class MemoAdapter(private val list: List<MyListItem>) :
        RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {

            val binding = MemoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )

            return MemoViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
            val resultTitle = list[position].title
            val resultSubTitle = list[position].subTitle
            val resultMemoType = list[position].memo_type
            val resultInputTime = list[position].input_time


            if (resultMemoType == "voice") {
                holder.binding.memoSubtitle.visibility = View.INVISIBLE
            }


            holder.binding.memoTitle.text = "제목 : $resultTitle"
            holder.binding.memoSubtitle.text = resultSubTitle
            holder.binding.memoInputTime.text = resultInputTime

            if (resultMemoType == "text") {
                holder.binding.memoType.setImageDrawable(resources.getDrawable(R.drawable.text))
            } else if (resultMemoType == "handwrite") {

                holder.binding.memoType.setImageDrawable(resources.getDrawable(R.drawable.handwriting))
            } else {
                holder.binding.memoType.setImageDrawable(resources.getDrawable(R.drawable.voice))
            }


        }


        override fun getItemCount(): Int {
            return list.size
        }


        internal inner class MemoViewHolder(var binding: MemoItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    }

    companion object {

        //TAG
        val TAG = "MultiMemoActivity"
        var TABLE_MEMO = "MEMO"
        var TABLE_VOICE = "VOICE"
        private val root = Environment.getExternalStorageDirectory().toString()
    }


}// Required empty public constructor
