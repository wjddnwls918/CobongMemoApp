package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentMemoListBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo.HandwriteViewActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo.HandwritingActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo.TextMemoViewActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo.TextMemoWriteActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo.VoicePlayFragment
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo.VoiceRecordFragment
import com.google.android.material.snackbar.Snackbar

//MemoDatabase database = MemoDatabase.Companion.getDatabase(getContext().getApplicationContext());

class MemoListFragment : Fragment(), View.OnClickListener, MemoNavigator {

    //DB
    private var binding: FragmentMemoListBinding? = null
    private lateinit var viewModel: MemoViewModel

    override fun onStart() {
        super.onStart()

        //load data
        loadList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo_list, container, false)

        viewModel = ViewModelProviders.of(this).get(MemoViewModel::class.java)


        viewModel!!.navigator = this

        //RecyclerView init
        initRecyclerView()

        //스크롤시 fab 없애기
        binding!!.rcvMemoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == SCROLL_STATE_DRAGGING) {
                    binding!!.fabAdd.hide()
                } else if (newState == SCROLL_STATE_IDLE) {

                    val anim = AnimationUtils.loadAnimation(context, R.anim.delay)
                    binding!!.fabAdd.animation = anim
                    binding!!.fabAdd.show()
                }
            }
        })


        binding!!.fabAdd.setOnClickListener(this)


        return binding!!.root
    }

    fun initRecyclerView() {
        //memoAdapter = MemoAdapter(ArrayList(), viewModel)
        viewModel.initAdapter()
        binding!!.rcvMemoList.layoutManager = LinearLayoutManager(context)
        binding!!.rcvMemoList.adapter = viewModel.getAdapter()
    }


    //load List or refresh List
    fun loadList() {

        viewModel.getAllMemos()
        viewModel.getAdapter()!!.setItem(viewModel.allMemos)

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
                            val intent = Intent(activity, TextMemoWriteActivity::class.java)
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
                            val intent3 = Intent(activity, HandwritingActivity::class.java)
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

    //메모 보기로 이동
    override fun sendMemo(item: MemoListItem) {

        val intent: Intent

        if (item.memoType == "text") {
            intent = Intent(context, TextMemoViewActivity::class.java)
            intent.putExtra("textItem", item)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else if (item.memoType == "voice") {

            val dialogFragment = VoicePlayFragment()
            val inputdate = Bundle()
            inputdate.putString("inputdate", item.voiceId)
            dialogFragment.arguments = inputdate
            dialogFragment.show(activity!!.supportFragmentManager, "tag")

        } else {
            intent = Intent(context, HandwriteViewActivity::class.java)
            intent.putExtra("handwriteItem", item)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }


    }


    override fun deleteMemo(memo: MemoListItem) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("경고")
            .setMessage("메모를 지우시겠습니까?")
            .setPositiveButton("닫기", null)
            .setNegativeButton("확인") { dialog, which ->

                //delete memo
                viewModel.deleteMemo(memo)
            }

        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()

    }


    override fun editMemo(memo: MemoListItem) {

        val intent: Intent

        if (memo.memoType == "text") {
            intent = Intent(activity, TextMemoWriteActivity::class.java)
            intent.putExtra("textItem", memo)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        } else if (memo.memoType == "handwrite") {
            intent = Intent(activity, HandwritingActivity::class.java)
            intent.putExtra("handwriteItem", memo)
            intent.putExtra("type", "edit")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        } else {
            Snackbar.make(view!!, "수정할 수 없습니다", Snackbar.LENGTH_SHORT).show()
        }
    }


}
