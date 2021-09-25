package HelperUtils


import com.typesafe.config.{Config, ConfigBeanFactory, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.*
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter
import org.cloudbus.cloudsim.datacenters.{Datacenter, DatacenterSimple}
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.hosts.network.NetworkHost
import org.cloudbus.cloudsim.network.topologies.{BriteNetworkTopology, NetworkTopology}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerSpaceShared, VmSchedulerTimeShared}
import HelperUtils.CreateLogger
import Simulations.ScalingSim
import org.cloudbus.cloudsim.core
import org.cloudbus.cloudsim.vms.Vm
import org.cloudsimplus.autoscaling.{HorizontalVmScaling, HorizontalVmScalingSimple}
import org.slf4j.Logger

import java.util
import java.util.logging.Level
import scala.jdk.CollectionConverters.*
import scala.util.Random


object commonutils {

  val logger = CreateLogger(classOf[core.Simulation])

  /**
   * Creates host with specified Scheduler as in configuration
   *
   * @param config Config file which contains the Datacenter/Hosts info
   * @return Host object with specified parameters
   */

  def createHost(config: Config ) = {
    val hostPes : Int = config.getInt("datacenter.host.pes")
    val peList = (1 to hostPes).map { _ =>
      new PeSimple(1000).asInstanceOf[Pe]
    }.toList
    val hosts_ram: Long = config.getLong("datacenter.host.ram")
    val storage: Long = config.getLong("datacenter.host.size")
    val host_bw: Long = config.getLong("datacenter.host.bw")
    val vmscheduler: String = config.getString("datacenter.host.vmscheduler")

    val host = new NetworkHost(hosts_ram, host_bw, storage, peList.asJava)
    host.setVmScheduler(vmscheduler match {
      case "SpaceShared" =>
        logger.info("Space Shared VM Scheduling is enabled.")
        new VmSchedulerSpaceShared()
      case "TimeShared" =>
        logger.info("Time Shared VM Scheduling is enabled.")
        new VmSchedulerTimeShared()
      case _ =>
        logger.warn("VM Scheduling algorithm should be either TimeShared or SpaceShared.")
        logger.info("Going ahead with TimeShared VM Scheduling.")
        new VmSchedulerTimeShared()

    })

  }


  def createHost2(config: Config ) = {
    val hostPes : Int = config.getInt("datacenter.host2.pes")
    val peList = (1 to hostPes).map { _ =>
      new PeSimple(1000).asInstanceOf[Pe]
    }.toList
    val hosts_ram: Long = config.getLong("datacenter.host2.ram")
    val storage: Long = config.getLong("datacenter.host2.size")
    val host_bw: Long = config.getLong("datacenter.host2.bw")
    val vmscheduler: String = config.getString("datacenter.host2.vmscheduler")


    val host = new NetworkHost(hosts_ram, host_bw, storage, peList.asJava)
    host.setVmScheduler(vmscheduler match {
      case "SpaceShared" =>
        logger.info("Space Shared VM Scheduling is enabled.")
        new VmSchedulerSpaceShared()
      case "TimeShared" =>
        logger.info("Time Shared VM Scheduling is enabled.")
        new VmSchedulerTimeShared()
      case _ =>
        logger.warn("VM Scheduling algorithm should be either TimeShared or SpaceShared.")
        logger.info("Going ahead with TimeShared VM Scheduling.")
        new VmSchedulerTimeShared()
    })

  }

  def createHost3(config: Config ) = {
    val hostPes : Int = config.getInt("datacenter.host3.pes")
    val peList = (1 to hostPes).map { _ =>
      new PeSimple(1000).asInstanceOf[Pe]
    }.toList
    val hosts_ram: Long = config.getLong("datacenter.host3.ram")
    val storage: Long = config.getLong("datacenter.host3.size")
    val host_bw: Long = config.getLong("datacenter.host3.bw")
    val vmscheduler: String = config.getString("datacenter.host3.vmscheduler")


    val host = new NetworkHost(hosts_ram, host_bw, storage, peList.asJava)
    host.setVmScheduler(vmscheduler match {
      case "SpaceShared" =>
        logger.info("Space Shared VM Scheduling is enabled.")
        new VmSchedulerSpaceShared()
      case "TimeShared" =>
        logger.info("Time Shared VM Scheduling is enabled.")
        new VmSchedulerTimeShared()
      case _ =>
        logger.warn("VM Scheduling algorithm should be either TimeShared or SpaceShared.")
        logger.info("Going ahead with TimeShared VM Scheduling.")
        new VmSchedulerTimeShared()
    })

  }
  /**
 * Sets a VM allocation Policy usingt the choices passed
 *
 * @param policy
 * @return [[VmAllocationPolicy]] object

  */
  def getVmAllocationPolicy(policy: String): VmAllocationPolicy = {
    policy match {
      case "FirstFit" => new VmAllocationPolicyFirstFit()
      case "BestFit" => new VmAllocationPolicyBestFit()
      case _ =>
        logger.warn("VM Allocation Policy should be either Simple, FirstFit or BestFit.")
        logger.info("Going ahead with Simple VM Allocation Policy.")
        new VmAllocationPolicySimple()
    }
  }

  /**
   * Creates scalable Datacenters based on the conf file
   *
   * @param cloudSim     CloudSim Object
   * @param hostList     List of hosts present in the Datacenter
   * @param config       Config file which contains the Datacenter/Hosts info
   * @return List of Datacenters
   */


  def createDatacenter (cloudSim: CloudSim,hostList: List[Host],config: Config, scheduling_Interval: Int = 0): Datacenter = {
    // Check type and create that DataCenter
    val dataCenter = config.getString("datacenter.dcType")  match {
      case "Simple" => new DatacenterSimple (cloudSim, hostList.asJava).setSchedulingInterval(scheduling_Interval)
      case "Network" => new NetworkDatacenter (cloudSim, hostList.asJava, getVmAllocationPolicy (config.getString ("datacenter.vmAllocationPolicy") ) ).setSchedulingInterval(scheduling_Interval)
      case _ =>
        logger.warn("Datacenter type should be either Simple or Network.")
        logger.info("Going ahead with creating a Simple Datacenter.")
        new DatacenterSimple (cloudSim, hostList.asJava)

    }

    // Set all the costs
    dataCenter.getCharacteristics
      .setCostPerBw(config.getDouble("datacenter.costPerBw"))
      .setCostPerMem(config.getDouble("datacenter.costPerMem"))
      .setCostPerSecond(config.getDouble("datacenter.costPerSecond"))
      .setCostPerStorage(config.getDouble("datacenter.costPerStorage"))

    dataCenter
  }

  /**
   * Not being used.
   *
   * @param logger          Instance of the Logger class
   * @param level           Level of the log, can be Trace, Debug, Info, Warn (Warning), Error
   * @param log             Log statement to be printed
   * @return
   */
  def LogLevel (logger: Logger, level: String, log: String) = {
    if (logger != null && level != null){
      level match {
        case "TRACE" => logger.trace(log)
        case "DEBUG" => logger.debug(log)
        case "INFO" => logger.info(log)
        case "WARN" => logger.warn(log)
        case "ERROR" => logger.error(log)
        case _ => Exception
      }
    }
  }


}
