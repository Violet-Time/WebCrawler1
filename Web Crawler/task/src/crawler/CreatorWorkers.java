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

/**
 * Создатель рабочих
 */

public class CreatorWorkers {

    //Максимальная глубина
    private int maxDepth = -1;
    //Максимальное время в секундах
    private int maxTime = -1;
    //Количество рабочих
    private int workersCount = 1;
    //Статус работы
    private int status = 0;
    private int hTime;

    //Сохранение ссылки и заголовка страницы
    HashMap<String, String> saveUrlAndTitle = new HashMap<>();
    //Ссылки на страниы для парсинга
    ConcurrentLinkedQueue<MyData> dataForCrawler = new ConcurrentLinkedQueue<>();
    AtomicInteger workersRunCount = new AtomicInteger(0);
    AtomicInteger parsedPages = new AtomicInteger(0);
    Worker[] workers;

    Timer timer;

    JLabel timerTextLabel;
    JLabel parsedCPagesTextLabel;

    CreatorWorkers(){

    }

    CreatorWorkers(int maxDepth, int maxTime, int workersCount, JLabel timerTextLabel, JLabel parsedCPagesTextLabel){
        this.maxDepth = maxDepth;
        this.maxTime = maxTime;
        this.timerTextLabel = timerTextLabel;
        this.parsedCPagesTextLabel = parsedCPagesTextLabel;
        this.workersCount = workersCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int gethTime() {
        return hTime;
    }

    public HashMap<String, String> getSaveUrlAndTitle() {
        return saveUrlAndTitle;
    }

    public ConcurrentLinkedQueue<MyData> getDataForCrawler() {
        return dataForCrawler;
    }

    public void timer(){

        this.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(maxTime > 0 && maxTime < hTime){
                    status = 0;
                }
                timerTextLabel.setText(String.valueOf(hTime));
                //parsedCPagesTextLabel.setText(String.valueOf(parsedPages.get()));
                hTime++;

            }
        });
    }

    void createWorkers() throws InterruptedException {
        //System.out.println(urls.peek()[1]);
        workers = new Worker[workersCount];
        status = 1;
        timer();
        for (int i = 0; i < workers.length; i++){
            workers[i] = new Worker(this);
            workers[i].start();
        }

    }

    void exportFile(String name){
        System.out.println(name);
        File file = new File(name);
        try(PrintWriter writer = new PrintWriter(file)){

            saveUrlAndTitle.forEach((k,v) -> writer.println(k + "\n" + v));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        status = 0;
        //throw new Flags(Flags.STOP_WORKER);
    }
}
