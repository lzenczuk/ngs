package com.github.lzenczuk.ngs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lzenczuk 13/08/2015
 */
public class Engine {

    private BlockingQueue<String> inQ = new LinkedBlockingQueue<>(256);

    private boolean outQ1Active = false;
    private BlockingQueue<String> outQ1 = new LinkedBlockingQueue<>(256);

    private boolean outQ2Active = false;
    private BlockingQueue<String> outQ2 = new LinkedBlockingQueue<>(256);

    public Engine() {

        new Thread(() -> {

            while(true){
                try {
                    String command = inQ.take();

                    try {
                        if (outQ1Active) outQ1.add(command);
                        if (outQ2Active) outQ2.add(command);
                    }catch(IllegalStateException e){
                        // skipping - it is not a solution
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void executeCommand(String command){
        try {
            inQ.add(command);
        }catch (IllegalStateException e){
            // skipping message
        }
    }

    public BlockingQueue<String> getOutputQ(){
        if(!outQ1Active){
            outQ1Active=true;
            return outQ1;
        }else if(!outQ2Active){
            outQ2Active=true;
            return outQ2;
        }else{
            throw new IllegalStateException("Both qs are connected. At this moment only two are supported");
        }
    }
}
