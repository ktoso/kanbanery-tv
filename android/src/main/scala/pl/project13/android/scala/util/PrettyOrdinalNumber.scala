package pl.project13.android.scala.util

/**
 * "Pretty print" utility.
 * Usage:
 * {{{
 * import tv.yap.common.format.PrettyOrdinalNumber._
 *
 * assert "3rd" == 3.ordinal
 * }}}
 *
 */
object PrettyOrdinalNumber {
  implicit def int2ordinal(n: Int) = new PrettyOrdinalNumber(n)
}

class PrettyOrdinalNumber(n: Long) {
  val ordinal = {
    val hundredRemainder = n % 100;
    val tenRemainder = n % 10;

    n + (if (hundredRemainder - tenRemainder == 10) "th";
    else if (n >= 11 && n <= 13) "th"
    else tenRemainder match {
      case 1 => "st"
      case 2 => "nd"
      case 3 => "rd"
      case _ => "th"
    })
  }
}

