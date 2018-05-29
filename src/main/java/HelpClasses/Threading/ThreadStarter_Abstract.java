/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package HelpClasses.Threading;

import Socket.SocketReader;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public abstract class ThreadStarter_Abstract extends Observable {

    /**
     *
     * @param runnable
     * @param executor
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    public void startThreadAndWaitForCompletition(Runnable runnable, ExecutorService executor) throws InterruptedException, ExecutionException {
        Future<?> future = executor.submit(runnable);

        future.get();   //Wait for thread to finish successfull
    }

    /**
     *
     * @param runnables
     * @param executor
     */
    public void startThreadsAndWaitForCompletition(List<Runnable> runnables, ExecutorService executor) {
        runnables.stream().map((runnable) -> executor.submit(runnable)).forEachOrdered((future) -> {
            try {
                future.get();   //Wait for thread to finish successfull
            } catch (ExecutionException | InterruptedException ex) {
                Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
