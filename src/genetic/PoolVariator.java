package genetic;

import corewar.Cell;

import java.util.List;

public class PoolVariator extends Pool {
    
    public PoolVariator(int nbThread) {
        super(nbThread);
    }

    public void setter(List<List<Cell>> programs, List<Cell> progToTest) {
        this.indexLastGame.set(0);

        this.progsFights = new PairProgs[programs.size()];
        
        for(int i=0;i<programs.size();++i)
            this.progsFights[i] = new PairProgs(progToTest, programs.get(i), progToTest.get(0).team, i);
    }

    @Override
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
        int[] res = new int[1];
        // Arrays.fill(res,0);  // By default java in array are initialized with 0
        for(PairProgs p : this.progsFights) {
            res[0] += p.getScore(p.t1);
        }
        return res;
    }
}
