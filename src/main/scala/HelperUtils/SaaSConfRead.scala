package HelperUtils

import com.typesafe.config.{Config, ConfigFactory}

class SaaSConfRead(confFile: String, dc_confFile: Config){

  val conf: Config = ConfigFactory.load(confFile)

  val cloudletConf: Config = conf.getObject("service.cloudlet").toConfig
  val vmConf: Config = dc_confFile.getObject("datacenter.vm").toConfig
  val DC_cloudletConf: Config = dc_confFile.getObject("datacenter.cloudlet").toConfig

  val CLOUDLET_NUMBER: Int = cloudletConf.getInt("number")
  val CLOUDLET_MIN_LEN: Int = cloudletConf.getInt("minLength")
  val CLOUD_MAX_LEN: Int = cloudletConf.getInt("maxLength")
  val CLOUDLET_OUTPUT_SIZE: Int = cloudletConf.getInt("outputSize")
  val CLOUDLET_RAM: Int = cloudletConf.getInt("ram")

  /**
   Read from the Datacenter config file
   */
  val CLOUDLET_UTI_MODEL : Double = DC_cloudletConf.getDouble("utilizationRatio")
  val CLOUDLET_PES: Int = DC_cloudletConf.getInt("pes")


  /**
   * SAAS has no control over the VMs, so reading the VMs specs from the Datacenter config file
   */

  val VM_NUM: Int = vmConf.getInt("vm-number")
  val VM_MIPS: Int = vmConf.getInt("vm-mips")
  val VM_SIZE: Int = vmConf.getInt("vm-size")
  val VM_RAM: Int = vmConf.getInt("vm-ram")
  val VM_BW: Int = vmConf.getInt("vm-bw")
  val VM_PES: Int = vmConf.getInt("vm-pes")

}
