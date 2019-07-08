package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.setting.SettingsActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityMainBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.MemoListFragment
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.ScheduleFragment
import com.google.android.material.tabs.TabLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: MainPagerAdapter
    private lateinit var binding: ActivityMainBinding

    //TedPermission
    internal var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            //SnackBarUtil.showSnackBar(binding.getRoot(), "Permission Granted");
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            SnackBarUtil.showSnackBar(binding.root, "Permission Denied\n$deniedPermissions")
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //메뉴 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.cobong_custom -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //권한
        checkDangerousPermissions()

        // SD Card checking
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Toast.makeText(this, R.string.no_sdcard_message, Toast.LENGTH_LONG).show()
            return
        } else {
            val externalPath = Environment.getExternalStorageDirectory().absolutePath
            if (!MemoApplication.ExternalChecked && externalPath != null) {
                MemoApplication.ExternalPath = externalPath + File.separator
                //Log.d(TAG, "ExternalPath : " + MemoApplication.ExternalPath);

                MemoApplication.FOLDER_PHOTO =
                    MemoApplication.ExternalPath + MemoApplication.FOLDER_PHOTO
                MemoApplication.FOLDER_VIDEO =
                    MemoApplication.ExternalPath + MemoApplication.FOLDER_VIDEO
                MemoApplication.FOLDER_VOICE =
                    MemoApplication.ExternalPath + MemoApplication.FOLDER_VOICE
                MemoApplication.FOLDER_HANDWRITING =
                    MemoApplication.ExternalPath + MemoApplication.FOLDER_HANDWRITING
                MemoApplication.DATABASE_NAME =
                    MemoApplication.ExternalPath + MemoApplication.DATABASE_NAME

                MemoApplication.ExternalChecked = true
            }
        }

        //
        setLocale()

        setUI()

    }

    fun setLocale() {
        //set current locale
        val curLocale = resources.configuration.locale
        MemoApplication.language = curLocale.language
    }

    private fun setUI() {

        pagerAdapter = MainPagerAdapter(supportFragmentManager, binding.tlMainTab.tabCount)
        binding.vpMain.adapter = pagerAdapter


        binding.vpMain.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tlMainTab))
        binding.tlMainTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpMain.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    inner class MainPagerAdapter(fm: FragmentManager, private val mPageCount: Int) :
        FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {
                    return MemoListFragment()
                }

                1 -> {
                    return ScheduleFragment()
                }

                else -> return null
            }
        }

        override fun getCount(): Int {
            return mPageCount
        }

    }


    /*android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.RECORD_AUDIO*/
    fun checkDangerousPermissions() {
        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            )
            .check()
    }

}
