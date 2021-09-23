import HelperUtils.{CreateLogger, ObtainConfigReference}
import Simulations.{BasicCloudSimPlusExample, DatacenterNetworking, IaasSim, PaasSim, SaasSim, ScalingSim}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object Simulation:
  val logger = CreateLogger(classOf[Simulation])

  @main def runSimulation =
    logger.info("Constructing a cloud model...")
    //BasicCloudSimPlusExample.Start()
    //IaasSim.Start()
    //PaasSim.Start()
    //SaasSim.Start()
    //DatacenterNetworking.Start()
    ScalingSim.Start()

    logger.info("Finished cloud simulation...")

class Simulation