import HelperUtils.{CreateLogger, ObtainConfigReference}
import Simulations.{BasicCloudSimPlusExample, IaasSim, SaasSim, PaasSim}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object Simulation:
  val logger = CreateLogger(classOf[Simulation])

  @main def runSimulation =
    logger.info("Constructing a cloud model...")
    //BasicCloudSimPlusExample.Start()
    IaasSim.Start()
    //PaasSim.Start()
    //SaasSim.Start()

    logger.info("Finished cloud simulation...")

class Simulation