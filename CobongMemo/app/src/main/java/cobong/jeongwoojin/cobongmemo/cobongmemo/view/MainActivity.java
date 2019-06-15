package cobong.jeongwoojin.cobongmemo.cobongmemo.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import cobong.jeongwoojin.cobongmemo.cobongmemo.R;
import cobong.jeongwoojin.cobongmemo.cobongmemo.SettingsActivity;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BasicInfo;
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil;
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityMainBinding;
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

    private MainPagerAdapter pagerAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //메뉴 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cobong_custom:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //권한
        checkDangerousPermissions();

        // SD Card checking
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, R.string.no_sdcard_message, Toast.LENGTH_LONG).show();
            return;
        } else {
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!BasicInfo.ExternalChecked && externalPath != null) {
                BasicInfo.ExternalPath = externalPath + File.separator;
                //Log.d(TAG, "ExternalPath : " + BasicInfo.ExternalPath);

                BasicInfo.FOLDER_PHOTO = BasicInfo.ExternalPath + BasicInfo.FOLDER_PHOTO;
                BasicInfo.FOLDER_VIDEO = BasicInfo.ExternalPath + BasicInfo.FOLDER_VIDEO;
                BasicInfo.FOLDER_VOICE = BasicInfo.ExternalPath + BasicInfo.FOLDER_VOICE;
                BasicInfo.FOLDER_HANDWRITING = BasicInfo.ExternalPath + BasicInfo.FOLDER_HANDWRITING;
                BasicInfo.DATABASE_NAME = BasicInfo.ExternalPath + BasicInfo.DATABASE_NAME;

                BasicInfo.ExternalChecked = true;
            }
        }

        //
        setLocale();

        setUI();

    }

    public void setLocale() {
        //set current locale
        Locale curLocale = getResources().getConfiguration().locale;
        BasicInfo.language = curLocale.getLanguage();
    }

    private void setUI() {

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), binding.tlMainTab.getTabCount());
        binding.vpMain.setAdapter(pagerAdapter);


        binding.vpMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tlMainTab));
        binding.tlMainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vpMain.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class MainPagerAdapter extends FragmentStatePagerAdapter {

        private int mPageCount;

        public MainPagerAdapter(FragmentManager fm, int pageCount) {
            super(fm);
            this.mPageCount = pageCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MemoListFragment memoListFragment = new MemoListFragment();
                    return memoListFragment;

                case 1:
                    ScheduleFragment scheduleFragment = new ScheduleFragment();
                    return scheduleFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

    }

    //TedPermission
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //SnackBarUtil.showSnackBar(binding.getRoot(), "Permission Granted");
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            SnackBarUtil.showSnackBar(binding.getRoot(), "Permission Denied\n" + deniedPermissions.toString());
        }

    };


    /*android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.RECORD_AUDIO*/
    public void checkDangerousPermissions() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .check();
    }

}
