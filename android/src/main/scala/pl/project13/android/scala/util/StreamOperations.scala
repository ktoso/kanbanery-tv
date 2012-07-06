package pl.project13.android.scala.util

import com.google.protobuf.CodedOutputStream
import java.io.{OutputStream, Closeable}

trait StreamOperations {

  def managed[C <: Closeable, T](closable: C)(block: (C) => T): T = {
    val result = block(closable)
    closable.close()
    result
  }

  def managedCoded[C <: OutputStream, T](os: C)(block: (CodedOutputStream) => T): T = {
    val cos = CodedOutputStream.newInstance(os)

    val result = block(cos)
    cos.flush()
    os.close()

    result
  }
}

object StreamOperations extends StreamOperations
