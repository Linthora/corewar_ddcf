package genetic;

import java.util.Set;
import corewar.*;

/**
 * Object used as Thread by the {@link Pool} to run duels between to programs.
 */
public class Duel extends Thread{
    
    /**
     * Core used by the object to run the duels.
     */
    protected Interpreter core;

    /**
     * Current task of the Thread.
     */
    protected PairProgs progs;

    /**
     * Reference of the pool connected to them.
     */
    protected Pool pool;

    /**
     * Id number of the Thread.
     */
    protected int id;

    /**
     * Create a Duel thread.
     * @param id Id number of the Thread.
     * @param pool Reference of the pool connected to them.
     */
    public Duel(int id, Pool pool) {
        // this.core = new Interpreter(progs.progs);
        this.id = id;
        this.pool = pool;
    }

    @Override
    public void run() {
        /// infinite loop to run the thread. 
        for(;;){
            try {
                synchronized(this) {
                    // wake up the pool when the thread is done with it's job.
                    this.pool.wakeUp();
                    // put to sleep the thread when there is no task for it to evaluate.
                    this.wait();
                }
            } catch (InterruptedException e) {
                // exit the infinite loop when the wait is interrupted. So the thread goes to its death.
                break;
            }
            
            // ask for the pool to give him a task if there is still.
            while(pool.getTask(this.id)){ 
                for(int i=0;i<5;++i) {
                    resetCore();
                    while(!core.isOver()) 
                        core.playNextTour();
                    Set<Integer> win = core.getWinners();
                    if(win.size()!=1) {
                        if(this.core.getTurnN() > 50L) { // 50 tmp limit
                            ++this.progs.results[0];
                            ++this.progs.results[1];
                        }
                    }
                    else if(win.contains(this.progs.t1)) {
                        if(this.core.getTurnN() > 50L)
                            this.progs.results[0]++;
                        if(this.core.getTurnN() > 100L)
                            this.progs.results[0]++;
                        if(this.core.getTurnN() > 200L)
                            this.progs.results[0]++;
                        if(this.core.getTurnN() < 300L)
                            this.progs.results[0]--;
                        this.progs.results[0]++;
                    } else {
                        if(this.core.getTurnN() > 50L)
                            this.progs.results[1]++;
                        if(this.core.getTurnN() > 100L)
                            this.progs.results[1]++;
                        if(this.core.getTurnN() > 200L)
                            this.progs.results[1]++;
                        if(this.core.getTurnN() < 300L)
                            this.progs.results[1]--;
                        this.progs.results[1]++;
                    }
                }
            }
            
        }
        // thread goes to death.
        System.out.println("Thread "+this.id+" has gone to the shadow realm.");
    }

    /**
     * Used by {@link Pool} to set the thread to run its next task.
     * @param programs
     */
    public void setTask(PairProgs programs) {
        this.progs = programs;
    }

    /**
     * Used by the Thread to reset the core for the evaluation its running.
     */
    public void resetCore() {
        this.core = new Interpreter(this.progs.progs);
    }

    @Override
    public String toString(){
        return "Thread " +this.id;
    }
}
