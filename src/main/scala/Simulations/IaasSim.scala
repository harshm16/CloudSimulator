package Simulations

import HelperUtils.{CreateLogger, IaaSConfRead, commonutils, createvmcloudlet_IaaS,Cost}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.core.CloudSim
import HelperUtils.commonutils.{createDatacenter, createHost}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter
import org.cloudbus.cloudsim.hosts.HostSimple
import org.cloudbus.cloudsim.resources.PeSimple
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import java.util
import scala.jdk.CollectionConverters.*

class IaasSim


object IaasSim:

  //Initiating logger object
  val logger = CreateLogger(classOf[IaasSim])


  def Start() =
    logger.info("Simulating Infrastructure As A Service (IaaS)")
    //Read the config file specific for IaaS
    val config: Config = ConfigFactory.load("DataCenter.conf")

    //Config file that can be modfied by the user/broker, contains info about VMs requested and Cloudlets
    val serviceModel = new IaaSConfRead("user_IaaS.conf")

    //Initiate Cloudsim
    val cloudsim = new CloudSim()

    //Create all hosts using the createHost function
    val hostList = (1 to config.getInt("datacenter.host.number")).map(_ => createHost(config)).toList
    logger.info("Created hosts")

    //Create datacenter using the above created hosts and a VM allocation policy
    //new NetworkDatacenter(cloudsim, hostList.asJava, new VmAllocationPolicyBestFit())
    createDatacenter(cloudsim, hostList,config)
    logger.info("Created Virtual machines.")

    //Create a Datacenter Broker
    val broker = new DatacenterBrokerSimple(cloudsim)

    //Class used to fetch the VMs and Cloudlets info from the config file and append them in separate lists
    val simpleJob = new createvmcloudlet_IaaS(cloudsim, broker, serviceModel)
    logger.info("Created a list of VMs and Cloudlets")

    //Submit the VMs list & Cloudlets to the Datacenter broker
    broker.submitVmList(simpleJob.createVMList.asJava)
    broker.submitCloudletList(simpleJob.createCloudletSimpleList.asJava)

    logger.info("Starting cloud simulation...")
    cloudsim.start()
    //val finishedCloudlets = broker.getCloudletFinishedList

    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    val createdVM = broker.getVmCreatedList()
    logger.info(s"Created VM list: $createdVM")
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    val CloudCreaetedList = broker.getCloudletCreatedList()
    logger.info(s"List of cloudlets created inside some Vm.: $CloudCreaetedList")
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList()
    logger.info(s"Finished Cloudlet list: $finishedCloudlets")
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")


    logger.info("Total cost of simulation = {} $", Cost.findCost(broker))
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    //Uses the CloudletsTableBuilder class to build a tabular result
    new CloudletsTableBuilder(broker.getCloudletFinishedList()).build()





