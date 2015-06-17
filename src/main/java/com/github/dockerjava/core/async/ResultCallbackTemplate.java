/*
 * Created on 16.06.2015
 */
package com.github.dockerjava.core.async;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.github.dockerjava.api.async.ResultCallback;

/**
 * Template implementation of {@link ResultCallback}
 *
 * @author marcus
 *
 */
public class ResultCallbackTemplate<T> implements ResultCallback<T>, Closeable {

    private final CountDownLatch finished = new CountDownLatch(1);

    private Closeable stream;

    @Override
    public void onStart(Closeable stream) {
        this.stream = stream;
    }

    @Override
    public void onResult(T object) {
    }

    @Override
    public void onError(Throwable throwable) {
        try {
            throw new RuntimeException(throwable);
        } finally {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onFinish() {
        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if(stream != null)
            stream.close();
        finished.countDown();
    }

    public void awaitFinish() throws InterruptedException {
        finished.await();
    }

    public void awaitFinish(long timeout, TimeUnit timeUnit) throws InterruptedException {
        finished.await(timeout, timeUnit);
    }
}
