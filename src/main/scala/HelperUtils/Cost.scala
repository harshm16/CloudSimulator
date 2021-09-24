package HelperUtils

import Simulations.SaasSim
import org.cloudbus.cloudsim.brokers.DatacenterBroker
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.vms.VmCost

import java.util
import java.util.Comparator
import scala.jdk.CollectionConverters.*
import scala.jdk.javaapi.CollectionConverters.asJava

class Cost

object Cost:

  val logger = CreateLogger(classOf[Cost])

  /**
   * Calculates the cost of an execution, based on the Datacenters cost configurations.
   *
   * @param boker          DatacenterBroker object.

   * @return Total cost of execution of the Simulation run.
   */
    def findCost(broker: DatacenterBroker) : Double = {

      var totalCost: Double = 0.0

      for (cl <- broker.getVmCreatedList.asScala) {
        val cost: VmCost = new VmCost(cl)

        logger.info("Working on datacenter {}, {}", cl.getLastTriedDatacenter.getId, cost)
        totalCost += cost.getTotalCost

      }
      totalCost

    }

