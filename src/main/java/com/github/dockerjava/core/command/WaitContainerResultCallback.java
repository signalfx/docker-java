/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import java.util.concurrent.TimeUnit;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.model.WaitResponse;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.google.common.base.Throwables;

/**
 *
 * @author marcus
 *
 */
public class WaitContainerResultCallback extends ResultCallbackTemplate<WaitContainerResultCallback, WaitResponse> {

    private final static Logger LOGGER = LoggerFactory.getLogger(WaitContainerResultCallback.class);

    @CheckForNull
    private WaitResponse waitResponse = null;

    @Override
    public void onNext(WaitResponse waitResponse) {
        this.waitResponse = waitResponse;
        LOGGER.debug(waitResponse.toString());
    }

    /**
     * Awaits the status code from the container.
     *
     * @throws DockerClientException
     *             if the wait operation fails.
     */
    public Integer awaitStatusCode() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }

        return getStatusCode();
    }

    /**
     * Awaits the status code from the container.
     *
     * @throws DockerClientException
     *             if the wait operation fails.
     */
    public Integer awaitStatusCode(long timeout, TimeUnit timeUnit) {
        try {
            awaitCompletion(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new DockerClientException("Awaiting status code interrupted: ", e);
        }

        return getStatusCode();
    }

    private Integer getStatusCode() {
        if (waitResponse == null) {
            throw new DockerClientException("Error while wait container");
        } else {
            return waitResponse.getStatusCode();
        }
    }
}
