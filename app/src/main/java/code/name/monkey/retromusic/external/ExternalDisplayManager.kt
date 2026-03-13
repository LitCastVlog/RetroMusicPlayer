package code.name.monkey.retromusic.external

import android.content.Context
import android.hardware.display.DisplayManager

class ExternalDisplayManager(private val context: Context) {

    private var presentation: ExternalDisplayPresentation? = null

    fun show() {
        val displayManager =
            context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION)

        if (displays.isNotEmpty()) {
            val display = displays[0]
            presentation = ExternalDisplayPresentation(context, display)
            presentation?.show()
        }
    }

    fun hide() {
        presentation?.dismiss()
        presentation = null
    }

    //Update song info
     
    fun updateSong(artist: String?, title: String, art: ByteArray? = null) {
        presentation?.updateSong(artist, title, art)
    }
}
