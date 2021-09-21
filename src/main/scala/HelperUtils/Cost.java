package HelperUtils;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Cost {

    private static final Logger logger = LoggerFactory.getLogger(Cost.class.getSimpleName());

    public static float executionCost(List<Cloudlet> cloudlets) {
        float cost = 0.0f;
        for (Cloudlet cl : cloudlets) {
            cost += cl.getTotalCost();
            logger.info("Cost to execute cloudlet {} on datacenter {} = {}"
                    , cl.getId(), cl.getLastTriedDatacenter().getId(), (float) cl.getTotalCost());
        }
        return cost;
    }
}
