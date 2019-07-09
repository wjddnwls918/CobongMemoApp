package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.placeInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EndlessRecyclerViewScrollListener
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityPlaceInfoBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.ScheduleNavigator
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.ScheduleViewModel


class PlaceInfoActivity : AppCompatActivity(), ScheduleNavigator {

    private lateinit var binding: ActivityPlaceInfoBinding
    private lateinit var viewModel: ScheduleViewModel

    private lateinit var placeInfoAdapter: PlaceInfoAdapter

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            cobong.jeongwoojin.cobongmemo.cobongmemo.R.layout.activity_place_info
        )

        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.navigator = this

        initToolbar()

        initRecyclerView()
        initSearchObserver()
        initKeyboardSearchListener()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()

            R.id.menu_search -> {
                searchPlaceByKeyword()
            }
        }

        return false
    }

    fun searchPlaceByKeyword() {

        KeyBoardUtil.hideSoftKeyboard(binding.root, this)

        placeInfoAdapter.removeAllItems()
        viewModel.isEnd = false
        viewModel.getKeywordPlace(
            resources.getText(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.rest_api_key).toString()
            , binding.etInputPlaceKeyword.text.toString()
            , 1
        )
    }

    fun initToolbar() {
        setSupportActionBar(binding.tbPlaceInfo);

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_place_info, menu);
        return true;
    }

    fun initRecyclerView() {

        val lm = LinearLayoutManager(this)
        binding.rcvPlaceInfo.layoutManager = lm

        scrollListener = object : EndlessRecyclerViewScrollListener(lm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("pagecheck", page.toString())

                viewModel.getKeywordPlace(
                    resources.getText(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.rest_api_key).toString()
                    , binding.etInputPlaceKeyword.text.toString()
                    , page + 1
                )
            }
        }

        binding.rcvPlaceInfo.addOnScrollListener(scrollListener)

        placeInfoAdapter = PlaceInfoAdapter(ArrayList<Document>(), viewModel)
        binding.rcvPlaceInfo.adapter = placeInfoAdapter


    }

    fun initSearchObserver() {
        viewModel.placeInfo.observe(this, Observer { data ->
            if (data.size != 0) {
                data.let {
                    placeInfoAdapter.addItems(data)
                }
            } else {
                SnackBarUtil.showSnackBar(
                    binding.root,
                    resources.getString(R.string.place_info_can_not_find_keyword)
                )
            }
        })
    }

    fun initKeyboardSearchListener() {
        binding.etInputPlaceKeyword.setOnEditorActionListener {

                v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPlaceByKeyword();
                true
            }
            false
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDocumentClick(document: Document) {
        val intent = Intent()
        intent.putExtra("result",document)
        setResult(101, intent)

        finish()
    }

}
