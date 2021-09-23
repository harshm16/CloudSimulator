package Simulations

import HelperUtils.commonutils.{createHost, getVmAllocationPolicy}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class testVMAllocationpolicy extends AnyFlatSpec with Matchers {
  behavior of "Check for valid VM Allocation Policy "

  it should "Set VM Allocation policy" in {

    val policy = "FirstFit"
    val policycrea = getVmAllocationPolicy(policy)
    policycrea shouldBe a [VmAllocationPolicy]
  }
}