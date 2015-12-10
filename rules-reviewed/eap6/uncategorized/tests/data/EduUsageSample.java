package environment.dependent;

import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.SynchronousQueue;
import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;

public class EduUsageSample{
   public EduUsageSample(){
   }

   public BlockingQueue createQueue(int queueCapacity){
     if (queueCapacity > 0) {
       return new LinkedBlockingQueue(queueCapacity);
     }
    else {
       return new SynchronousQueue();
     }
   }
}
