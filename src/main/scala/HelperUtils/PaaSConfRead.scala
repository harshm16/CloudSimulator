package HelperUtils

import com.typesafe.config.{Config, ConfigFactory}

class PaaSConfRead(confFile: String, dc_confFile: Config){

  val conf: Config = ConfigFactory.load(confFile)

  val cloudletConf: Config = conf.getObject("service.cloudlet").toConfig
  val vmConf: Config = conf.getObject("service.vm").toConfig
  val DC_vmConf: Config = dc_confFile.getObject("datacenter.vm").toConfig


  val CLOUDLET_NUMBER: Int = cloudletConf.getInt("number")
  val CLOUDLET_MIN_LEN: Int = cloudletConf.getInt("minLength")
  val CLOUD_MAX_LEN: Int = cloudletConf.getInt("maxLength")
  val CLOUDLET_OUTPUT_SIZE: Int = cloudletConf.getInt("outputSize")
  val CLOUDLET_RAM: Int = cloudletConf.getInt("ram")
  val CLOUDLET_UTI_MODEL : Double = cloudletConf.getDouble("utilizationRatio")
  val CLOUDLET_PES: Int = cloudletConf.getInt("pes")


  /**
   * In PAAS the user can only controler the number of VMs they reqest.
   */

  val VM_NUM: Int = vmConf.getInt("vm-number")

  /**
   * Other VM configurations are read from the Datacenter config file
   */

  val VM_MIPS: Int = DC_vmConf.getInt("vm-mips")
  val VM_SIZE: Int = DC_vmConf.getInt("vm-size")
  val VM_RAM: Int = DC_vmConf.getInt("vm-ram")
  val VM_BW: Int = DC_vmConf.getInt("vm-bw")
  val VM_PES: Int = DC_vmConf.getInt("vm-pes")

}
