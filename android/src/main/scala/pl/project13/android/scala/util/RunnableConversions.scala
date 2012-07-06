package pl.project13.android.scala.util

trait RunnableConversions {

  implicit def named_block_runnable[T](block: => T) = new Runnable {
    def run() {
      block
    }
  }

  implicit def fun0_runnable[T](block: () => T) = new Runnable {
    def run() {
      block()
    }
  }

}

object RunnableConversions extends RunnableConversions
