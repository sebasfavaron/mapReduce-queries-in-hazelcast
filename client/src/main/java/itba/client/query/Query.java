package itba.client.query;

import java.util.concurrent.ExecutionException;

public interface Query {

    void run() throws InterruptedException, ExecutionException;

}
