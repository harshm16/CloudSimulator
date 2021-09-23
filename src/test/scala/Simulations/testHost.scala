package Simulations

import HelperUtils.commonutils.{createDatacenter, createHost}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.hosts.Host
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class testHost extends AnyFlatSpec with Matchers {
  behavior of "Check if Host is created "

  it should "create a host" in {

    val config: Config = ConfigFactory.load("DataCenter.conf")
    val hostList = createHost(config)

    hostList shouldBe a [Host]
  }
}
