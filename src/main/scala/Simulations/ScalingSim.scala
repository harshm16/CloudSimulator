package Simulations

import HelperUtils.commonutils.{createDatacenter, createHost}
import HelperUtils.scalingutil.{createInitialScalableVms}
import HelperUtils.{Cost, CreateLogger, SaaSConfRead, createvmcloudlet_SaaS}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudsimplus.listeners.EventInfo

import java.util
import scala.::
import scala.jdk.CollectionConverters.*

class ScalingSim
object ScalingSim:

  //Initiating logger object
  val logger = CreateLogger(classOf[ScalingSim])


  def Start() =
    logger.info("Simulating Software As A Service (SaaS) which provides Horizontal Scaling.")
    //Read the config file specific for IaaS
    val config: Config = ConfigFactory.load("DataCenter.conf")

    logger.info("Reading user defined config.")
    //Config file that can be modfied by the user/broker, contains info about VMs requested and Cloudlets
    val serviceModel = new SaaSConfRead("user_SaaS.conf", config)

    //Initiate Cloudsim
    val cloudsim = new CloudSim()
    logger.info("Initiating Cloud Sim.")

    //Create all hosts using the createHost function
    val hostList = (1 to config.getInt("datacenter.host.number")).map(_ => createHost(config)).toList
    logger.info(s"Created hosts")

    //Create datacenter using the above created hosts and a VM allocation policy
    //new NetworkDatacenter(cloudsim, hostList.asJava, new VmAllocationPolicyBestFit())
    val SCHEDULING_INTERVAL: Int = 5
    val CLOUDLETS_CREATION_INTERVAL = SCHEDULING_INTERVAL * 2

    createDatacenter(cloudsim, hostList,config,SCHEDULING_INTERVAL)
    logger.info(s"Created a scalable Datacenter.")

    //Create a Datacenter Broker
    val broker = new DatacenterBrokerSimple(cloudsim)
    broker.setVmDestructionDelay(10.0)
    //Class used to fetch the VMs and Cloudlets info from the config file and append them in separate lists
    val simpleJob = new createvmcloudlet_SaaS(cloudsim, broker, serviceModel)
    logger.info(s"Created a list of Cloudlets")

    val vmQuantity = config.getInt("datacenter.vm.vm-number")

    val vmList = createInitialScalableVms(simpleJob,vmQuantity)
    logger.info(s"Created a list of Scalable VMs")

    //Submit the VMs list & Cloudlets to the Datacenter broker
    broker.submitVmList(vmList.asJava)

    broker.submitCloudletList(simpleJob.createCloudletSimpleList.asJava)

    logger.info("Starting cloud simulation...")
    cloudsim.start()
    //val finishedCloudlets = broker.getCloudletFinishedList
    //createNewCloudlets(CLOUDLETS_CREATION_INTERVAL,simpleJob,broker)
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





