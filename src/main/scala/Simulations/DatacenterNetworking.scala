package Simulations

import HelperUtils.commonutils.{createDatacenter, createHost, createHost2, createHost3}
import HelperUtils.{Cost, CreateLogger, IaaSConfRead, PaaSConfRead,SaaSConfRead, createvmcloudlet_IaaS, createvmcloudlet_PaaS, createvmcloudlet_SaaS}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.network.topologies.{BriteNetworkTopology, NetworkTopology}
import org.cloudbus.cloudsim.vms.Vm
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import java.util
import java.util.{ArrayList, List}
import java.util.Comparator
import scala.jdk.CollectionConverters.*
import scala.jdk.javaapi.CollectionConverters.asJava

class DatacenterNetworking

object DatacenterNetworking:

  //Initiating logger object
  val logger = CreateLogger(classOf[DatacenterNetworking])


  def Start() =
    logger.info("Simulating Multiple Datacenter Networking architecture")
    logger.info("Networking Architecture based on Brite Topology.")


    //Initiate Cloudsim
    val cloudsim = new CloudSim()

    //Read the config file specific for IaaS
    val config: Config = ConfigFactory.load("DataCenter.conf")

    //Config file that can be modfied by the user/broker, contains info about VMs requested and Cloudlets
    val iaasModel = new IaaSConfRead("user_IaaS.conf")
    val paasModel = new PaaSConfRead("user_PaaS.conf", config)
    val saasModel = new SaaSConfRead("user_SaaS.conf", config)

    //Create all hosts using the createHost function
    val hostList = (1 to config.getInt("datacenter.host.number")).map(_ => createHost(config)).toList
    logger.info(s"Created IaaS hosts: $hostList")

    val hostList2 = (1 to config.getInt("datacenter.host2.number")).map(_ => createHost2(config)).toList
    logger.info(s"Created PaaS hosts: $hostList2")

    val hostList3 = (1 to config.getInt("datacenter.host3.number")).map(_ => createHost3(config)).toList
    logger.info(s"Created SaaS hosts: $hostList3")

    //Create datacenter using the above created hosts and a VM allocation policy
    val dc1 = createDatacenter(cloudsim, hostList,config)
    logger.info(s"Created IaaS Datacenter: $dc1")
    val dc2 = createDatacenter(cloudsim, hostList2,config)
    logger.info(s"Created PaaS Datacenter: $dc2")
    val dc3 = createDatacenter(cloudsim, hostList3,config)
    logger.info(s"Created SaaS Datacenter: $dc3")

    //Create a Datacenter Broker
    val broker = new DatacenterBrokerSimple(cloudsim)

    //Create network topology from a brite file
    val networkTopology: BriteNetworkTopology = BriteNetworkTopology.getInstance("topology.brite")
    cloudsim.setNetworkTopology(networkTopology)

    //Datacenter1 mapped to BRITE node 0
    networkTopology.mapNode(dc1, 0)

    //Datacenter2 mapped to BRITE node 2
    networkTopology.mapNode(dc2, 2)

    //Datacenter3 mapped to BRITE node 3
    networkTopology.mapNode(dc3, 3)

    //Broker mapped to BRITE node 4
    networkTopology.mapNode(broker, 4)

    
    //Class used to fetch the VMs and Cloudlets info from the config file and append them in separate lists
    val vmcloudlet_Iaas = new createvmcloudlet_IaaS(cloudsim, broker, iaasModel)
    val vmcloudlet_Paas = new createvmcloudlet_PaaS(cloudsim, broker, paasModel)
    val vmcloudlet_Saas = new createvmcloudlet_SaaS(cloudsim, broker, saasModel)
    logger.info("Created a list of VMs and Cloudlets")

    //Submit the VMs list & Cloudlets to the Datacenter broker
    // create and submit Vms to broker
    val vmList: util.List[Vm] = new util.ArrayList[Vm]

    vmList.addAll(vmcloudlet_Iaas.createVMList.asJava)
    vmList.addAll(vmcloudlet_Paas.createVMList.asJava)
    vmList.addAll(vmcloudlet_Saas.createVMList.asJava)


    broker.submitVmList(vmList)
    logger.info(s"Submited the list of VMs to the Datacenter broker: $vmList")

    // create and submit Cloudlets to broker
    val cloudletList = new util.ArrayList[Cloudlet]

    cloudletList.addAll(vmcloudlet_Iaas.createCloudletSimpleList.asJava)
    cloudletList.addAll(vmcloudlet_Paas.createCloudletSimpleList.asJava)
    cloudletList.addAll(vmcloudlet_Saas.createCloudletSimpleList.asJava)

    broker.submitCloudletList(cloudletList)
    logger.info(s"Submited the list of Cloudlets to the Datacenter broker: $cloudletList")


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






