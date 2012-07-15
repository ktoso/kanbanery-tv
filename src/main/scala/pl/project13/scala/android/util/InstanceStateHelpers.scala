package pl.project13.scala.android.util

import android.os.Bundle

case class InstanceStateKey[+T](key: String)(implicit manifest: Manifest[T]) {

  def tryGet(instanceState: Bundle): Option[T] = {
    if (instanceState == null) return None

    if(instanceState.containsKey(key)) {
      Some(instanceState.getSerializable(key).asInstanceOf[T])
    } else {
      None
    }
  }

  def get(instanceState: Bundle): T = {
    instanceState.getSerializable(key).asInstanceOf[T]
  }
}

trait InstanceStateHelpers {
  def saveInstanceState[T <: Serializable](outState: Bundle)(key: InstanceStateKey[T], obj: T) {
    outState.putSerializable(key.key, obj)
  }

  def saveInstanceStateInt(outState: Bundle)(key: InstanceStateKey[Int], obj: Int) {
    outState.putInt(key.key, obj)
  }

  def saveInstanceStateLong(outState: Bundle)(key: InstanceStateKey[Long], obj: Long) {
    outState.putLong(key.key, obj)
  }
}