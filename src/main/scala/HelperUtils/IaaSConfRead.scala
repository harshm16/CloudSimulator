package HelperUtils

import com.typesafe.config.{Config, ConfigFactory}

class IaaSConfRead(confFile: String){

  val conf: Config = ConfigFactory.load(confFile)
  val vmConf: Config = conf.getObject("service.vm").toConfig
  val cloudletConf: Config = conf.getObject("service.cloudlet").toConfig

  val CLOUDLET_NUMBER: Int = cloudletConf.getInt("number")
  val CLOUDLET_PES: Int = cloudletConf.getInt("pes")
  val CLOUDLET_MIN_LEN: Int = cloudletConf.getInt("minLength")
  val CLOUD_MAX_LEN: Int = cloudletConf.getInt("maxLength")
  val CLOUDLET_OUTPUT_SIZE: Int = cloudletConf.getInt("outputSize")
  val CLOUDLET_RAM: Int = cloudletConf.getInt("ram")
  val CLOUDLET_UTI_MODEL : Double = cloudletConf.getDouble("utilizationRatio")


  /**
   * IAAS also has control over the VMs and Host machine it needs
   */

  val VM_NUM: Int = vmConf.getInt("vm-number")
  val VM_MIPS: Int = vmConf.getInt("vm-mips")
  val VM_SIZE: Int = vmConf.getInt("vm-size")
  val VM_RAM: Int = vmConf.getInt("vm-ram")
  val VM_BW: Int = vmConf.getInt("vm-bw")
  val VM_PES: Int = vmConf.getInt("vm-pes")

}
