package Simulations

import HelperUtils.commonutils.{createDatacenter, createHost}
import HelperUtils.{Cost, CreateLogger, IaaSConfRead, SaaSConfRead, createvmcloudlet_IaaS, createvmcloudlet_SaaS}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import java.util
import scala.jdk.CollectionConverters.*

class SaasSim

object SaasSim:

  //Initiating logger object
  val logger = CreateLogger(classOf[SaasSim])


  def Start() =
    logger.info("Simulating Software As A Service (SaaS)")
    //Read the config file specific for IaaS
    val config: Config = ConfigFactory.load("DataCenter.conf")

    //Config file that can be modfied by the user/broker, contains info about VMs requested and Cloudlets
    val serviceModel = new SaaSConfRead("user_SaaS.conf", config)

    //Initiate Cloudsim
    val cloudsim = new CloudSim()

    //Create all hosts using the createHost function
    val hostList = (1 to config.getInt("datacenter.host.number")).map(_ => createHost(config)).toList
    logger.info(s"Created hosts: $hostList")

    //Create datacenter using the above created hosts and a VM allocation policy
    //new NetworkDatacenter(cloudsim, hostList.asJava, new VmAllocationPolicyBestFit())
    createDatacenter(cloudsim, hostList,config)
    logger.info("Created Datacenter.")

    //Create a Datacenter Broker
    val broker = new DatacenterBrokerSimple(cloudsim)

    //Class used to fetch the VMs and Cloudlets info from the config file and append them in separate lists
    val simpleJob = new createvmcloudlet_SaaS(cloudsim, broker, serviceModel)
    logger.info("Created a list of VMs and Cloudlets")

    //Submit the VMs list & Cloudlets to the Datacenter broker
    broker.submitVmList(simpleJob.createVMList.asJava)
    broker.submitCloudletList(simpleJob.createCloudletSimpleList.asJava)
    logger.info("Submitted the list of VMs and Cloudlets to the broker.")

    logger.info("Starting cloud simulation...")
    cloudsim.start()
    //val finishedCloudlets = broker.getCloudletFinishedList

    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    val createdVM = broker.getVmCreatedList()
    logger.info(s"Created VM list: $createdVM")
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    val CloudCreatedList = broker.getCloudletCreatedList()
    logger.info(s"List of cloudlets created inside some Vm.: $CloudCreatedList")
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList()
    logger.info(s"Finished Cloudlet list: $finishedCloudlets")
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    logger.info("Total cost of simulation = {} $", Cost.findCost(broker))
    System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n")

    //Uses the CloudletsTableBuilder class to build a tabular result
    new CloudletsTableBuilder(broker.getCloudletFinishedList()).build()






