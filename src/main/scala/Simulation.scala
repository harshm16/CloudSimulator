import HelperUtils.{CreateLogger, ObtainConfigReference}
import Simulations.{BasicCloudSimPlusExample, DatacenterNetworking, IaasSim, PaasSim, SaasSim, ScalingSim}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object Simulation:
  val logger = CreateLogger(classOf[Simulation])

  @main def runSimulation =
    logger.info("Constructing a cloud model...")

    //Please uncomment to run the respective Simulaions.

    /***
    Basic Cloud Simulation Example:
    Already implemented in the Git repo: git@github.com:0x1DOCD00D/CloudOrgSimulator.git.
    */
    //BasicCloudSimPlusExample.Start()

    /***
    Infrastructure as a Service Simulation:
    The user controls the configuration of the requested VMs
    as well as the specifications of the Cloudlets running on the VMs.

    The config file which can be modified by the user: user_IaaS.conf
    */
    //IaasSim.Start()

    /***
    Platform as a Service Simulation:
    The user only controls the number of the requested VMs, but not their configurations.
    They do control the specifications of the Cloudlets to be run.

    The config file which can be modified by the user: user_PaaS.conf
    */
    //PaasSim.Start()

    /***
    Software as a Service Simulation:
    The user has no control over any specicifactions of the VMs, they only control the number & size
    of Cloudlets they want to run on the VMs.

    The config file which can be modified by the user: user_SaaS.conf
     */
    //SaasSim.Start()

    /***
    Datacenter Networking Simulation:
    Multiple Datacenters providing different types of services are connected using the Brite topology.

    The config file which can be modified by the user: user_IaaS.conf, user_PaaS.conf, user_SaaS.conf
     */
    DatacenterNetworking.Start()

    /***
    Horizontal Scaling Simulation:
    A Datacenter which provides Software as a Service provides a scalable architecture.
    i.e. if the number of Cloudlets running exceed the limit which the number of VMs present can manage,
    more VMs will be created by the Datacenter.

    The config file which can be modified by the user: user_SaaS.conf
     */
    //ScalingSim.Start()

    logger.info("Finished cloud simulation...")

class Simulation