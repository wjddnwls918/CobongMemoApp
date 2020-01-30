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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EndlessRecyclerViewScrollListener
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityPlaceInfoBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document


class PlaceInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceInfoBinding

    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: PlaceInfoViewModel

    private lateinit var placeInfoAdapter: PlaceInfoAdapter

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_place_info
        )


        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlaceInfoViewModel::class.java).apply {
            binding.viewmodel = this
        }

        initToolbar()

        initRecyclerView()
        initSearchObserver()
        initKeyboardSearchListener()
        setupNavigation()
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

    private fun setupNavigation() {
        viewModel.documentClickEvent.observe(this, EventObserver {
            val intent = Intent()
            intent.putExtra("result",it)
            setResult(101, intent)

            finish()
        })

    }

    fun initToolbar() {
        setSupportActionBar(binding.tbPlaceInfo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_place_info, menu)
        return true
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

                _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPlaceByKeyword()
            }
            false
        }
    }

    override fun onBackPressed() {
        finish()
    }
/*
    override fun onDocumentClick(document: Document) {
        val intent = Intent()
        intent.putExtra("result",document)
        setResult(101, intent)

        finish()
    }*/

}
