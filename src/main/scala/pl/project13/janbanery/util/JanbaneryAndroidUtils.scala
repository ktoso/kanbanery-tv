package pl.project13.janbanery.util

import android.graphics.Color

object JanbaneryAndroidUtils {

  /**
   * Converts a color, as given by Kanbanery, to a Android readable format.
   * This is needed as sometimes Kanbanery returns "#222", which should be "#222222".
   * Then, `Color.parseColor(String)` is being called.
   *
   * @param colorString the string to assure that it's parseable by android
   * @return the parsed color
   */
  def toAndroidColor(colorString: String) = {
    val preparedString = if (colorString.matches("\\#[0-9A-Fa-f]{3}")) {
      (new StringBuilder)
        .append("#")
        .append(colorString.charAt(1)).append(colorString.charAt(1))
        .append(colorString.charAt(2)).append(colorString.charAt(2))
        .append(colorString.charAt(3)).append(colorString.charAt(3))
        .toString()
    } else colorString

    Color.parseColor(preparedString)
  }
}
