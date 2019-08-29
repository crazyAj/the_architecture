package cn.aj.commons.utils.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by itw_yangwj on 2018/7/13.
 */
public class DisruptorExceptionHander<T> implements ExceptionHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger("disruptor");

    public DisruptorExceptionHander() {
    }

    @Override
    public void handleEventException(Throwable throwable, long squence, T event) {
        logger.error("------- handleEventException ------- {},{},{}", squence, event, throwable);
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        logger.error("------- handleOnStartException ------- {}", throwable);
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        logger.error("------- handleOnShutdownException ------- {}", throwable);
    }
}
