package cn.aj.service;

import cn.aj.commons.utils.disruptor.AbstractBatchDisruptor;
import cn.aj.domain.entity.InvertEvent;

/**
 * Created by itw_yangwj on 2018/7/17.
 */
public abstract class BatchDisruptorService<T, V> extends AbstractBatchDisruptor<InvertEvent<T, V>, V> {

    public BatchDisruptorService() {
        super();
    }
    public BatchDisruptorService(int bufferSize) {
        super(bufferSize);
    }
    public BatchDisruptorService(int bufferSize, int firstParallelSize) {
        super(bufferSize, firstParallelSize);
    }
    public BatchDisruptorService(int bufferSize, int firstParallelSize, int secondParallelSize) {
        super(bufferSize, firstParallelSize, secondParallelSize);
    }
    public BatchDisruptorService(int bufferSize, int firstParallelSize, int secondParallelSize, int thirdParallelSize) {
        super(bufferSize, firstParallelSize, secondParallelSize, thirdParallelSize);
    }

    @Override
    public InvertEvent<T, V> newInstance() {
        return new InvertEvent<>();
    }

    @Override
    public void translateTo(InvertEvent<T, V> event, long sequence, V v) {
        event.setV(v);
    }

    @Override
    public void onFirstEvent(InvertEvent<T, V> event) {
        V v = event.getV();
        T t = packageData(v);
        event.setT(t);
    }
    @Override
    public void onSecondEvent(InvertEvent<T, V> event) {
        V v = event.getV();
        recordData(v);
    }
    @Override
    public void onThirdEvent(InvertEvent<T, V> event) {
        T t = event.getT();
        sendData(t);
    }

    public abstract T packageData(V v);
    public abstract void recordData(V v);
    public abstract void sendData(T t);

}
