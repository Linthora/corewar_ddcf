package genetic;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import corewar.*;

public class Pool {

    /**
     * Array of Duel (the threads) of the pool.
     */
    Duel[] pool;

    /**
     * Array of tasks that needs to be processed by the pool.
     */
    PairProgs[] progsFights;

    /**
     * Integer indicating the index of the next task to be processed by the thread.
     * AtomicInteger allows to synchronise the repartition of the tasks to the threads.
     */
    AtomicInteger indexLastGame;

    /**
     * Integer indicating the number of threads that are currently done with their tasks so that the pool knows when to wake up.
     * AtomicIntegers allows to synchronise the signal of the threads to wake up the pool when they're done.
     */
    AtomicInteger finishedThread;

    /**
     * Creates a Pool with the specified amount of threads.
     * @param nbThread number of threads to creates.
     */
    public Pool(int nbThread) {

        this.indexLastGame = new AtomicInteger();
        this.finishedThread = new AtomicInteger();
        this.pool = new Duel[nbThread];

        for(int i=0;i<nbThread;++i) {
            this.pool[i] = new Duel(i, this);
            this.pool[i].start();
        }
        
    }

    /**
     * Set the tasks to be processed by the pool.
     * @param programs
     */
    public void setter(List<List<Cell>> programs) {
        // reset task counter.
        this.indexLastGame.set(0);

        // create the new array of tasks.
        this.progsFights = new PairProgs[(programs.size()*(programs.size()-1)/2)];
        int counter=0;
        for(int i=0;i<programs.size();++i)
            for(int j=i+1;j<programs.size();++j)
                this.progsFights[counter++] = new PairProgs(programs.get(i), programs.get(j), i, j);
    }

    /**
     * Launch the evaluation of the programs previously turned into tasks with the setter.
     * @param nbProgs Number of programs to evaluate.
     * @return Array of the scores of each program.
     */
    public int[] play(int nbProgs) {
        // reset threads counter.
        this.finishedThread.set(0);
        
        // wake up the threads now that we have the tasks ready.
        for(int i=0;i<this.pool.length;++i)
            synchronized(this.pool[i]){
                this.pool[i].notify();
            }

        // sending the pool in wait state until the threads are done.
        try {
            while(this.finishedThread.get() < this.pool.length) {
                synchronized(this){
                    if(this.finishedThread.get() >= this.pool.length)
                        break;
                    wait();
                } 
            }
        } catch (InterruptedException e) { // if an error occurs with the pool there's a serious problem with the usage of the pool. This has never occured, even in the process of making the pool and testing it. 
            e.printStackTrace();
            System.err.println("Coup dur...");
            System.exit(10);
        }
        
        // Creating the score array and filling it.
        int[] res = new int[nbProgs];
        // Arrays.fill(res,0);  // By default java in array are initialized with 0
        for(PairProgs p : this.progsFights) {
            res[p.t1] += p.getScore(p.t1);
            res[p.t2] += p.getScore(p.t2);
        }
        return res;
    }


    /**
     * Method called by threads to see if there are still tasks for them to do and if there is set it for them.
     * @param id Id of the thread asking for task.
     * @return True if there is a task for them to do, False otherwise.
     */
    public boolean getTask(int id) {
        int tmp = indexLastGame.getAndIncrement();
        if(tmp < this.progsFights.length) {
            this.pool[id].setTask(progsFights[tmp]);
            return true;
        }
        return false;
    }

    /**
     * Method called by threads to send signal to the pool when they are done.s
     */
    public void wakeUp(){
        this.finishedThread.getAndIncrement();
        synchronized(this){
            notify();
        }
    }

    /**
     * Method to end the threads when we are done with the pool.
     */
    public void shutThread() {
        System.out.println("shutting down...");
        for(int i=0;i<this.pool.length;++i)
            this.pool[i].interrupt();
    }
}
