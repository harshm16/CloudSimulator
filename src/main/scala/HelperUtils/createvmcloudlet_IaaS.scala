package HelperUtils

import org.cloudbus.cloudsim.brokers.DatacenterBroker
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.Simulation
import org.cloudbus.cloudsim.hosts.Host
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic, UtilizationModelFull}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}

class createvmcloudlet_IaaS(var simulation: Simulation,
                            var broker: DatacenterBroker,
                            val serviceModel: IaaSConfRead) {


  def createVMList: List[Vm] =
    (1 to serviceModel.VM_NUM).map(_ => new VmSimple(serviceModel.VM_MIPS, serviceModel.VM_PES)
      .setRam(serviceModel.VM_RAM)
      .setBw(serviceModel.VM_BW)
      .setSize(serviceModel.VM_SIZE)).toList


  def createCloudletSimpleList: List[Cloudlet] = {
    //val utilizationModel: UtilizationModelFull = new UtilizationModelFull()
    val utilizationModel = new UtilizationModelDynamic(serviceModel.CLOUDLET_UTI_MODEL)
    (1 to serviceModel.CLOUDLET_NUMBER).map ( _ =>
      new CloudletSimple(serviceModel.CLOUD_MAX_LEN, serviceModel.CLOUDLET_PES, utilizationModel)
        .setOutputSize(serviceModel.CLOUDLET_OUTPUT_SIZE)
    ).toList
  }


}
  

