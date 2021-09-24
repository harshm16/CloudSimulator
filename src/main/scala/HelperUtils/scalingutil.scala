package HelperUtils

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.vms.Vm
import org.cloudsimplus.autoscaling.{HorizontalVmScaling, HorizontalVmScalingSimple}
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudsimplus.listeners.EventInfo

import java.util
import scala.jdk.CollectionConverters.*


object scalingutil {

  def createInitialScalableVms(simpleJob: createvmcloudlet_SaaS, vmNum: Int): List[Vm] =
    (1 to vmNum).map { _ =>
      val vm: Vm = simpleJob.createVM
      createHorizontalVmScaling(vm,simpleJob)
      
      vm
    }.toList


  def createHorizontalVmScaling(vm: Vm, simpleJob: createvmcloudlet_SaaS): Unit = {
    val horizontalVmScaling: HorizontalVmScaling = new HorizontalVmScalingSimple()

    horizontalVmScaling
      .setVmSupplier(() => simpleJob.createVM)
      .setOverloadPredicate(isVmOverloaded)

    vm.setHorizontalScaling(horizontalVmScaling)
  }

  def isVmOverloaded(vm: Vm): Boolean = {
    vm.getCpuPercentUtilization > 0.05
  }

}
