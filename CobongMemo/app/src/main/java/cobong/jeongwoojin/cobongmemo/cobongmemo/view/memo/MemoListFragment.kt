package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentMemoListBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo.HandwriteViewActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo.HandwritingActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo.TextMemoViewActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo.TextMemoWriteActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo.VoicePlayFragment
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo.VoiceRecordFragment
import com.google.android.material.snackbar.Snackbar

class MemoListFragment : Fragment(), View.OnClickListener, View.OnTouchListener {

    private lateinit var binding: FragmentMemoListBinding

    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: MemoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            cobong.jeongwoojin.cobongmemo.cobongmemo.R.layout.fragment_memo_list,
            container,
            false
        )

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MemoViewModel::class.java).apply {
                binding.viewmodel = this
            }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        //RecyclerView init
        initRecyclerView()
        //initObserveLivedata()
        setupNavigation()

    }

    fun initRecyclerView() {

        ItemTouchHelper(SwipeToDeleteCallback(activity!!.applicationContext, viewModel)).apply {
            attachToRecyclerView(binding.rcvMemoList)
        }

        //스크롤시 fab 없애기
        binding.rcvMemoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    binding.fabAdd.hide()
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val anim = AnimationUtils.loadAnimation(
                        context,
                        cobong.jeongwoojin.cobongmemo.cobongmemo.R.anim.delay
                    )
                    binding.fabAdd.animation = anim
                    binding.fabAdd.show()
                }
            }
        })

        binding.fabAdd.setOnClickListener(this@MemoListFragment)
        initKeyboardSearchListener()

    }

    private fun setupNavigation() {

        //메모 보기
        viewModel.openMemoEvent.observe(this.viewLifecycleOwner, EventObserver {
            sendMemo(it)
        })

        //삭제
        viewModel.deleteMemoEvent.observe(this.viewLifecycleOwner, EventObserver {
            deleteMemo(it)
        })

        //수정
        viewModel.editMemoEvent.observe(this.viewLifecycleOwner, EventObserver {
            editMemo(it)
        })

        //스와이프
        viewModel.swipedEvent.observe(this.viewLifecycleOwner, EventObserver {
            if (viewModel._alertFlag.value != true) {
                makeDialog {
                    //delete memo
                    viewModel.deleteByListPosition(it)
                }
            }
        })
    }

    fun initKeyboardSearchListener() {

        binding.tietInputMemoFilter.setOnTouchListener(this)
        binding.tietInputMemoFilter.setOnEditorActionListener {

                _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                setFilter()
            }
            false
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.fab_add -> {

                val memoType = arrayOf<CharSequence>(
                    context?.resources!!.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.text),
                    context?.resources!!.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.voice),
                    context?.resources!!.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.handwrite)
                )

                AlertDialog.Builder(context).apply {
                    setTitle(context?.resources!!.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.select_memo_type))
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val DRAWABLE_RIGHT = 2

        if (event?.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (binding.tietInputMemoFilter.getRight() - binding.tietInputMemoFilter.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                setFilter()
                return true
            }
        }
        return false
    }

    fun setFilter() {
        viewModel.setFilteredList(binding.tietInputMemoFilter.text.toString())
    }

    //메모 보기로 이동
    fun sendMemo(item: MemoItem) {
        selectMemoType(item,
            {
                val intent = Intent(context, TextMemoViewActivity::class.java)
                intent.putExtra("textItem", item)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }, {
                val dialogFragment = VoicePlayFragment()
                val voiceBundle = Bundle()
                voiceBundle.putParcelable("voiceItem", item)
                dialogFragment.arguments = voiceBundle
                dialogFragment.show(activity!!.supportFragmentManager, "tag")
            }, {
                val intent = Intent(context, HandwriteViewActivity::class.java)
                intent.putExtra("handwriteItem", item)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        )
    }


    fun deleteMemo(memo: MemoItem) {

        if (viewModel._alertFlag.value != true) {
            makeDialog {
                //delete memo
                viewModel.deleteByRoom(memo)
            }
        }
    }


    fun editMemo(memo: MemoItem) {

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
            this.setMessage("메모를 지우시겠습니까?")
            this.setPositiveButton("취소") { _, _ ->
                viewModel._alertFlag.value = false
                viewModel.adapter.notifyDataSetChanged()
            }
            setNegativeButton("확인") { _, _ ->

                dialogProcess()

                viewModel._alertFlag.value = false
            }

        }.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }

}
