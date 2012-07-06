package pl.project13.smsser.android.backend

import android.util.Log
import pl.project13.android.scala.util.StreamOperations
import java.net.{URLConnection, HttpURLConnection, URL}
import java.io.{InputStream, OutputStream}
import com.google.protobuf.{CodedOutputStream, GeneratedMessage}

class SmsserBackendClient(conf: BackendConfig) extends StreamOperations {

  import RequestMethod._

  type WritableProtoMessage = {
    def writeTo(os: CodedOutputStream)
  }

  val Tag = getClass.getSimpleName

  val ProtobufMediaType = "application/x-protobuf"

  def getProto[ProtoMessage](path: String)(implicit messageType: Manifest[ProtoMessage]): Either[Throwable, ProtoMessage] =
    onProtoURLConnection(path) { urlc =>
      try {
        managed(urlc.getInputStream) { entityStream =>
          val response = parse[ProtoMessage](from = entityStream)

          Right(response)
        }
      } catch {
        case x: Exception => Left(x)
      }
  }


  def postProto[ResponseProtoType](path: String, proto: GeneratedMessage)(implicit responseManifest: Manifest[ResponseProtoType]): Either[Exception, ResponseProtoType] =
    onProtoURLConnection(path) { urlc =>
      try {
        urlc.setRequestMethod(Post)
        managedCoded(urlc.getOutputStream) { proto writeTo _ }
        val response = managed(urlc.getInputStream) { is => parse[ResponseProtoType](from = is) }

        Right(response)
      } catch {
        case x: Exception => Left(x)
      }
    }

  def parse[ProtoMessageType](from: InputStream)(implicit messageTypeManifest: Manifest[ProtoMessageType]): ProtoMessageType = {
    val newBuilder = messageTypeManifest.erasure.getMethod("newBuilder")
    val builder = newBuilder.invoke(messageTypeManifest).asInstanceOf[GeneratedMessage.Builder[_ <: GeneratedMessage.Builder[_]]]
    val response = builder.mergeFrom(from).build.asInstanceOf[ProtoMessageType]
    response
  }

  def onProtoURLConnection[T](path: String)(block: (HttpURLConnection) => T) = {
    val url = conf.url + ("/" + path).replaceAll("//", "/")

    val urlc: HttpURLConnection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
    urlc.setDoInput(true)
    urlc.setDoOutput(true)
    urlc.setRequestProperty("Accept", ProtobufMediaType)
    urlc.setRequestProperty("Content-Type", ProtobufMediaType)

    tryDebug("Prepared urlConnection to: " + url)
    val result = block(urlc)

    urlc.disconnect()

    result
  }

  // todo replace with Logging trait
  def tryDebug(msg: String) = {
    try {
      Log.d(Tag, msg)
    } catch {
      case _ =>
    }
  }
}
