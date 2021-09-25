package Simulations

import Simulations.BasicCloudSimPlusExample.config
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BasicCloudSimPlusExampleTestSuite extends AnyFlatSpec with Matchers {
  behavior of "configuration parameters module"

  it should "obtain the utilization ratio" in {
    config.getDouble("cloudSimulator.utilizationRatio") shouldBe 0.8E0
  }

  it should "obtain the MIPS capacity" in {
    config.getDouble("cloudSimulator.vm.mipsCapacity") shouldBe 1000
  }
}
