package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentMemoListBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoItem
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
    private lateinit var binding: FragmentMemoListBinding
    private lateinit var viewModel: MemoViewModel
    private lateinit var memoAdapter: MemoAdapter

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


        viewModel.navigator = this

        //RecyclerView init
        initRecyclerView()

        initObserveLivedata()

        //스크롤시 fab 없애기
        binding.rcvMemoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == SCROLL_STATE_DRAGGING) {
                    binding.fabAdd.hide()
                } else if (newState == SCROLL_STATE_IDLE) {

                    val anim = AnimationUtils.loadAnimation(context, R.anim.delay)
                    binding.fabAdd.animation = anim
                    binding.fabAdd.show()
                }
            }
        })

        binding.fabAdd.setOnClickListener(this)

        return binding.root
    }

    fun initObserveLivedata() {
        viewModel.allMemosByRoom.observe(this, Observer {
            memos -> memos.let {
            memoAdapter.setItem(it)
        }
        })
    }

    fun initRecyclerView() {

       /* viewModel.getAllMemos()
        memoAdapter = MemoAdapter(viewModel.allMemos, viewModel)*/

        memoAdapter = MemoAdapter(ArrayList(), viewModel)

        binding.rcvMemoList.layoutManager = LinearLayoutManager(context)
        binding.rcvMemoList.adapter = memoAdapter
    }


    //load List or refresh List
    fun loadList() {

       /* viewModel.getAllMemos()
        memoAdapter.setItem(viewModel.allMemos)*/

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_add -> {

                val memoType = arrayOf<CharSequence>(
                    context?.resources!!.getString(R.string.text),
                    context?.resources!!.getString(R.string.voice),
                    context?.resources!!.getString(R.string.handwrite)
                )

                AlertDialog.Builder(context).apply {
                    setTitle(context?.resources!!.getString(R.string.select_memo_type))
                    setItems(memoType) { _, which ->
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
                                dialogFragment.setOnDismissListener(
                                    DialogInterface.OnDismissListener() {
                                       onStart()
                                    }
                                )

                                dialogFragment.show(activity!!.supportFragmentManager, "tag")
                            }

                            2 -> {
                                val intent3 = Intent(activity, HandwritingActivity::class.java)
                                intent3.putExtra("type", "insert")
                                startActivity(intent3)
                            }
                        }
                    }

                }.create().apply { show() }
            }
        }


    }

    //메모 보기로 이동
    override fun sendMemo(item: MemoItem) {
        selectMemoType(item,
            {
                val intent = Intent(context, TextMemoViewActivity::class.java)
                intent.putExtra("textItem", item)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }, {
                val dialogFragment = VoicePlayFragment()
                val inputdate = Bundle()
                inputdate.putString("inputdate", item.voiceId)
                dialogFragment.arguments = inputdate
                dialogFragment.show(activity!!.supportFragmentManager, "tag")
            }, {
                val intent = Intent(context, HandwriteViewActivity::class.java)
                intent.putExtra("handwriteItem", item)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        )
    }


    override fun deleteMemo(memo: MemoItem) {

        makeDialog {
            //delete memo
            //viewModel.deleteMemo(memo, memoAdapter)
            viewModel.deleteByRoom(memo)
            //memoAdapter.removeItem(memo)
        }
    }


    override fun editMemo(memo: MemoItem) {

        selectMemoType(memo,
            {
                val intent = Intent(activity, TextMemoWriteActivity::class.java)
                intent.putExtra("textItem", memo)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            },
            { Snackbar.make(view!!, "수정할 수 없습니다", Snackbar.LENGTH_SHORT).show() },
            {
                val intent = Intent(activity, HandwritingActivity::class.java)
                intent.putExtra("handwriteItem", memo)
                intent.putExtra("type", "edit")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        )
    }

    fun selectMemoType(
        memo: MemoItem, text: () -> Unit, voice: () -> Unit,
        handwrite: () -> Unit
    ) {
        when (memo.memoType) {
            "text" -> text()

            "voice" -> voice()

            else -> handwrite()
        }
    }

    fun makeDialog(dialogProcess: () -> Unit) {
        AlertDialog.Builder(context).apply {

            this.setTitle("경고")
            this.setMessage("메모를 지우시겠습니까?")
            this.setPositiveButton("닫기", null)
            setNegativeButton("확인") { _, _ ->

                dialogProcess()

            }

        }.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }

}
