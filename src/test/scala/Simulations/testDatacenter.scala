package Simulations

import HelperUtils.commonutils.{createDatacenter, createHost}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.Datacenter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class testDatacenter extends AnyFlatSpec with Matchers {
  behavior of "Check if Datacenter is created "

  it should "create a datacenter" in {
    val cloudsim = new CloudSim()
    val config: Config = ConfigFactory.load("DataCenter.conf")
    val hostList = (1 to config.getInt("datacenter.host.number")).map(_ => createHost(config)).toList

    val DC = createDatacenter(cloudsim, hostList,config)

    DC shouldBe a [Datacenter]
  }
}