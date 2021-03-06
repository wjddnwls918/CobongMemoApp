package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityMainBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.MemoListFragment
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.ScheduleFragment
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.todolist.TodoListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val FINISH_INTERVAL_TIME = 2000
    private var backPressedTime: Long = 0

    private val memoFragment = MemoListFragment()
    private val scheduleFragment = ScheduleFragment()
    private val todoListFragment = TodoListFragment()

    override fun onStart() {
        super.onStart()
        Log.d("lifecyclecheck","onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecyclecheck","onResume()")
    }

    override fun onPause() {
        Log.d("lifecyclecheck","onPause()")
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("lifecyclecheck","onStop()")
    }

    override fun onDestroy() {
        Log.d("lifecyclecheck","onDestroy()")
        super.onDestroy()
    }


    //TedPermission
    internal var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                this@MainActivity,
                applicationContext.getText(R.string.permission_denied_finish_activity).toString(),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecyclecheck","onCreate()")

        binding = DataBindingUtil.setContentView(
            this,
            cobong.jeongwoojin.cobongmemo.cobongmemo.R.layout.activity_main
        )

        //권한
        checkDangerousPermissions()

        // SD Card checking
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Toast.makeText(
                this,
                cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.no_sdcard_message,
                Toast.LENGTH_LONG
            ).show()
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
        var curLocale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            curLocale = resources.configuration.locales.get(0)
        } else
            curLocale = resources.configuration.locale
        MemoApplication.language = curLocale.language
    }

    private fun setUI() {

        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.fl_main, memoFragment)
            .commitAllowingStateLoss()

        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->

            transaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.action_memo -> {
                    transaction.replace(
                        cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.fl_main,
                        memoFragment
                    ).commitAllowingStateLoss()
                }

                cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.action_todo_list -> {
                    transaction.replace(
                        cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.fl_main,
                        todoListFragment
                    ).commitAllowingStateLoss()
                }

                cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.action_schedule -> {
                    transaction.replace(
                        cobong.jeongwoojin.cobongmemo.cobongmemo.R.id.fl_main,
                        scheduleFragment
                    ).commitAllowingStateLoss()
                }
            }

            true
        })


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

    override fun onBackPressed() {

        var tempTime = System.currentTimeMillis()
        var intervalTime = tempTime - backPressedTime

        //첫 번째 클릭
        when {
            0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime -> {
                super.onBackPressed()
            }
            else -> {
                backPressedTime = tempTime
                Toast.makeText(
                    this,
                    resources.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.finish_app),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
