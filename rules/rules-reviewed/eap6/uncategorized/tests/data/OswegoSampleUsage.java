package environment.dependent;

import edu.oswego.cs.dl.util.concurrent.PooledExecutor;
import edu.oswego.cs.dl.util.concurrent.BoundedBuffer

public class OswegoSampleUsage{
    
    //initialize to use a maximum of 8 threads.
    static PooledExecutor pool = new PooledExecutor(8);
    
   public OswegoSampleUsage(){
       pool = new PooledExecutor(new BoundedBuffer(10), 100);
       pool.setMinimumPoolSize(4);
       pool.setKeepAliveTime(1000 * 60 * 5);
       pool.createThreads(9);
   }

}