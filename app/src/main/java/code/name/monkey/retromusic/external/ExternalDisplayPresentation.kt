package code.name.monkey.retromusic.external

import android.app.Presentation
import android.content.*
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Bundle
import android.view.Display
import android.widget.ImageView
import android.widget.TextView
import code.name.monkey.retromusic.R
import java.text.SimpleDateFormat
import java.util.*

class ExternalDisplayPresentation(
    context: Context,
    display: Display
) : Presentation(context, display) {

    private lateinit var titleText: TextView
    private lateinit var albumArt: ImageView
    private lateinit var timeText: TextView
    private lateinit var batteryText: TextView

    private val infoReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateTimeAndBattery()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.external_display)

        titleText = findViewById(R.id.songTitle)
        albumArt = findViewById(R.id.albumArt)
        timeText = findViewById(R.id.timeInfo)
        batteryText = findViewById(R.id.batteryInfo)

        // Marquee only scrolls if the view is selected
        titleText.isSelected = true

        updateTimeAndBattery()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_TIME_CHANGED)
        }
        context.registerReceiver(infoReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        context.unregisterReceiver(infoReceiver)
    }

    private fun updateTimeAndBattery() {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        timeText.text = sdf.format(Date())

        val batteryStatus: Intent? = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        if (level != -1) {
            batteryText.text = "$level%"
        }
    }

    
      //change format to "Artist - Title"
    
    fun updateSong(artist: String?, title: String, art: ByteArray? = null) {
        val displayText = if (!artist.isNullOrEmpty()) "$artist - $title" else title
        titleText.text = displayText

        // Ensure marquee keeps scrolling
        titleText.isSelected = true

        if (art != null) {
            val bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
            albumArt.setImageBitmap(bitmap)
        } else {
            albumArt.setImageResource(R.drawable.default_audio_art)
        }
    }
}
