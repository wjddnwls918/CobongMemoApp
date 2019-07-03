package cobong.jeongwoojin.cobongmemo.cobongmemo


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.MainActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}
