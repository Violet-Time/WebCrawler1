package crawler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CreatorWorkers {

    private int maxDepth;
    private int maxTime;
    private int status;
    private int hTime;

    HashMap<String, String> map = new HashMap<>();
    ConcurrentLinkedQueue<MyData> urls = new ConcurrentLinkedQueue<>();
    AtomicInteger workersRunCount = new AtomicInteger(0);
    AtomicInteger parsedPages = new AtomicInteger(0);
    Worker[] workers;

    Timer timer;

    CreatorWorkers(int maxDepth, int maxTime, int workersCount, JLabel timerTextLabel, JLabel parsedCPagesTextLabel){
        this.maxDepth = maxDepth;
        this.maxTime = maxTime;
        this.workers = new Worker[workersCount];
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(maxTime > 0 && maxTime < hTime){
                    status = 0;
                }
                timerTextLabel.setText(String.valueOf(hTime));
                parsedCPagesTextLabel.setText(String.valueOf(parsedPages.get()));
                hTime++;

            }
        });
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public int getStatus() {
        return status;
    }

    public int gethTime() {
        return hTime;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public ConcurrentLinkedQueue<MyData> getUrls() {
        return urls;
    }

    void createWorkers() throws InterruptedException {
        //System.out.println(urls.peek()[1]);
        status = 1;
        for (int i = 0; i < workers.length; i++){
            workers[i] = new Worker(this);
            workers[i].start();
        }

    }

    void exportFile(String name){
        File file = new File(name);
        try(PrintWriter writer = new PrintWriter(file)){

            map.forEach((k,v) -> writer.println(k + " " + v));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        status = 0;
        //throw new Flags(Flags.STOP_WORKER);
    }
}
