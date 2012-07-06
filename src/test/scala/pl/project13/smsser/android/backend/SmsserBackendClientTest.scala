package pl.project13.smsser.android.backend

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import pl.project13.smsser.proto.gen.Sms.{SmsStoreResponse, DeviceIs, SmsStoreRequest}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, FunSuite}
import pl.project13.smsser.android.test.{TestBackendConfig, Any}


class SmsserBackendClientTest extends BackendIntegrationFlatSpec with ShouldMatchers {

  val backendClient = new SmsserBackendClient(TestBackendConfig())

  behavior of "SmsserBackendClient"

  it should "post a proto message" in {
    // given

    val storeRequest = SmsStoreRequest.newBuilder
      .setDeviceId(Any.deviceId)
      .setDeviceIs(DeviceIs.SENDER)
      .addMessages(Any.smsMessageDetails())
      .build()

    storeRequest.getDeviceId should not be (null)
    storeRequest.getDeviceIs should not be (null)

    // when
    val either = backendClient.postProto[SmsStoreResponse]("/sms", storeRequest)

    // then
    info("got: " + either)
    either should be ('right)
  }

}
