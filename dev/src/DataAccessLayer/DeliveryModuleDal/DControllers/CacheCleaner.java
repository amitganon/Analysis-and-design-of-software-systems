package DataAccessLayer.DeliveryModuleDal.DControllers;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheCleaner {

    List<DalController> mappers;
    private final int intervalTime = 5;

    public CacheCleaner(List<DalController> mappers) {
        this.mappers = mappers;
    }

    public void clean() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            for(DalController mapper : mappers)
                mapper.cleanCache();
        };
        executor.scheduleWithFixedDelay(task, 0, intervalTime, TimeUnit.MINUTES);
    }

}
