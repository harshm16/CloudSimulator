package HelperUtils

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.vms.Vm
import org.cloudsimplus.autoscaling.{HorizontalVmScaling, HorizontalVmScalingSimple}
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudsimplus.listeners.EventInfo

import java.util
import scala.jdk.CollectionConverters.*


object scalingutil {

  /**
   * Creates List of scalable VMs
   *
   * @param simpleJob     createvmcloudlet_SaaS Object
   * @param vmNum        Number of VMs to be created.
   *
   * @return List of Scalable VMs
   */
  def createInitialScalableVms(simpleJob: createvmcloudlet_SaaS, vmNum: Int): List[Vm] =
    (1 to vmNum).map { _ =>
      val vm: Vm = simpleJob.createVM
      createHorizontalVmScaling(vm,simpleJob)

      vm
    }.toList

  /**
   * Sets Horizontal Scaling to the VMs
   *
   * @param vm            The VM which is to be made scalabe.
   * @param simpleJob     createvmcloudlet_SaaS Object
   *
   */
  def createHorizontalVmScaling(vm: Vm, simpleJob: createvmcloudlet_SaaS): Unit = {
    val horizontalVmScaling: HorizontalVmScaling = new HorizontalVmScalingSimple()

    horizontalVmScaling
      .setVmSupplier(() => simpleJob.createVM)
      .setOverloadPredicate(isVmOverloaded)

    vm.setHorizontalScaling(horizontalVmScaling)
  }

  /**
   * Defines the Overloading policy, on which the Horizontal scaling depends.
   *
   * @param vm            The VM which is to be made scalabe.
   *
   * @return Boolean
   */
  def isVmOverloaded(vm: Vm): Boolean = {
    vm.getCpuPercentUtilization > 0.16
  }

}
