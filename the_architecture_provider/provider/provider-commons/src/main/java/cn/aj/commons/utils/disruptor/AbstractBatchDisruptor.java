package cn.aj.commons.utils.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by itw_yangwj on 2018/7/13.
 */
@Slf4j
public abstract class AbstractBatchDisruptor<T, V> implements EventFactory<T>, EventTranslatorOneArg<T, V> {

    private RingBuffer<T> ringBuffer;
    private Disruptor<T> disruptor;

    private int bufferSize = 2 << 10;
    private int firstParallelSize = 4;
    private int secondParallelSize = 0;
    private int thirdParallelSize = 0;

    public AbstractBatchDisruptor() {
        start();
    }

    public AbstractBatchDisruptor(int bufferSize) {
        this.bufferSize = bufferSize;
        start();
    }

    public AbstractBatchDisruptor(int bufferSize, int firstParallelSize) {
        this.bufferSize = bufferSize;
        this.firstParallelSize = firstParallelSize;
        start();
    }

    public AbstractBatchDisruptor(int bufferSize, int firstParallelSize, int secondParallelSize) {
        this.bufferSize = bufferSize;
        this.firstParallelSize = firstParallelSize;
        this.secondParallelSize = secondParallelSize;
        start();
    }

    public AbstractBatchDisruptor(int bufferSize, int firstParallelSize, int secondParallelSize, int thirdParallelSize) {
        this.bufferSize = bufferSize;
        this.firstParallelSize = firstParallelSize;
        this.secondParallelSize = secondParallelSize;
        this.thirdParallelSize = thirdParallelSize;
        start();
    }

    /**
     * 获取RingBuffer
     */
    public RingBuffer<T> getRingBuffer() {
        if (ringBuffer != null)
            return ringBuffer;
        throw new RuntimeException("This disruptor has not start!");
    }

    /**
     * 事件工厂
     */
    public abstract T newInstance();

    /**
     * 启动Disruptor
     * Consumer如何等待新事件，这是策略模式的应用。 Disruptor 提供了多个WaitStrategy的实现。
     * 每种策略都具有不同性能和优缺点， 根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的JVM的配置参数，能够实现不同的性能提升。
     * 例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
     *       BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
     *       SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
     *       YieldingWaitStrategy的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
     */
    public void start() {
        //初始化Disruptor任务调度器
        disruptor = new Disruptor<>(this, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new SleepingWaitStrategy());
        //覆盖默认异常类
        disruptor.setDefaultExceptionHandler(new DisruptorExceptionHander<>());
        //分配消费任务
        distributeTask();
        //调度器指定RingBuffer
        ringBuffer = disruptor.start();
    }

    /**
     * 关闭Disruptor后，会一直等所有任务执行完成后关闭
     */
    public void stop() {
        if (disruptor != null) disruptor.shutdown();
        disruptor = null;
        ringBuffer = null;
    }

    /**
     * 调用此方法，等待线程池中的所有任务执行完毕
     */
    public void await() {
        while (!handleCompletion()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("AbstractBathDisruptor await InterruptedException:{}", e);
            }
        }
    }

    /**
     * 上一批数据的完成情况
     */
    public boolean handleCompletion() {
        long cursor = this.getRingBuffer().getCursor();//当前数据光标位置
        long minimumGatingSequence = this.getRingBuffer().getMinimumGatingSequence();//最慢的消费者的位置
        return minimumGatingSequence == cursor;
    }

    /**
     * 生产者提交的数据与消费者的需要的数据之间的关系
     */
    public abstract void translateTo(T event, long sequence, V value);

    /**
     * 提交不成功会一直阻塞。生产数可以超过消费数，可以超过bufferSize，但是会阻塞等待
     */
    public void add(V value) {
        ringBuffer.publishEvent(this, value);
    }
    /**
     * 批量提交，values的长度不能超过bufferSize
     */
    public void add(V[] values) {
        if (values.length > bufferSize / 8) {
            V[] firstHalf = Arrays.copyOfRange(values, 0, values.length / 2);
            ringBuffer.publishEvents(this, firstHalf);
            V[] secondHalf = Arrays.copyOfRange(values, values.length / 2, values.length);
            ringBuffer.publishEvents(this, secondHalf);
        } else ringBuffer.publishEvents(this, values);
    }
    /**
     * 批量提交，list的长度不能超过bufferSize
     */
    public void add(Collection<V> values) {
        V[] value = (V[]) values.toArray();
        add(value);
    }

    /**
     * 指定任务策略，默认只有三个任务链，如果有更复杂的任务链请覆盖此方法
     */
    private void distributeTask() {
        if (firstParallelSize <= 0) throw new RuntimeException("The firstParallelSize can't less than 0!");
        WorkHandler<T>[] firstWorks = new WorkHandler[firstParallelSize];
        for (int i = 0; i < firstParallelSize; i++) {
            firstWorks[i] = new FirstWorkTask();
        }
        EventHandlerGroup<T> eventHandlerGroup = disruptor.handleEventsWithWorkerPool(firstWorks);
        if (secondParallelSize > 0) {
            WorkHandler<T>[] secondWorks = new WorkHandler[secondParallelSize];
            for (int i = 0; i < secondParallelSize; i++) {
                secondWorks[i] = new SecondWorkTask();
            }
            eventHandlerGroup = eventHandlerGroup.thenHandleEventsWithWorkerPool(secondWorks);
        }
        if (thirdParallelSize > 0) {
            WorkHandler<T>[] thirdWorks = new WorkHandler[thirdParallelSize];
            for (int i = 0; i < thirdParallelSize; i++) {
                thirdWorks[i] = new ThirdWorkTask();
            }
            eventHandlerGroup.thenHandleEventsWithWorkerPool(thirdWorks);
        }
    }

    /**
     * 第一批任务
     */
    private class FirstWorkTask implements WorkHandler<T> {
        @Override
        public void onEvent(T event) throws Exception {
            onFirstEvent(event);
        }
    }
    public abstract void onFirstEvent(T event);

    /**
     * 第二批任务
     */
    private class SecondWorkTask implements WorkHandler<T> {
        @Override
        public void onEvent(T event) throws Exception {
            onSecondEvent(event);
        }
    }
    public abstract void onSecondEvent(T event);

    /**
     * 第三批任务
     */
    private class ThirdWorkTask implements WorkHandler<T> {
        @Override
        public void onEvent(T event) throws Exception {
            onThirdEvent(event);
        }
    }
    public abstract void onThirdEvent(T event);

}
