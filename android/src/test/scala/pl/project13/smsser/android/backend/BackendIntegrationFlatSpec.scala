package pl.project13.smsser.android.backend

import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import pl.softwaremill.common.util.io.KillableProcess

trait BackendIntegrationFlatSpec extends FlatSpec with BeforeAndAfterAll {

  private val ServerId = 77777

  val serverStartupTime = 5000
  val smsserHome = System.getenv("SMSSER_HOME")

  assert(!smsserHome.isEmpty, "env.SMSSER_HOME must be set in order to run end-to-end integration tests!")

  private var apiServerProcess: KillableProcess = _

  override def beforeAll() {
    apiServerProcess = new KillableProcess(smsserHome + "/run-api-server-local.sh " + ServerId, "running-api-server="+ServerId)
    apiServerProcess.start()

    println("Starting api server...")
    Thread.sleep(serverStartupTime)
  }

  override def afterAll() {
    if(apiServerProcess != null) {
      apiServerProcess.sendSigKill()
    }
  }
}
