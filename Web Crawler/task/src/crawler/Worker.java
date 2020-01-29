package crawler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker extends Thread {


    CreatorWorkers workers;

    Worker(CreatorWorkers workers){
        this.workers = workers;
    }


    void parse(int depth, String url) throws IOException {

        //System.out.println(depth);
        System.out.println(url);
        if((workers.getMaxDepth() == -1 || depth <= workers.getMaxDepth()) && !workers.getSaveUrlAndTitle().containsKey(url)) {

            TextWebPage webPage  = new TextWebPage();

            if (webPage.getWebPage(url) != null) {

                //System.out.println(webPage.getTheWebPageText());

                Matcher linksMatcher = Pattern.compile(
                        "href=[\"\'][A-Za-z%\\d:\\/.]+"
                ).matcher(webPage.getTheWebPageText());

                //System.out.println(depth);
                //System.out.println(workers.getMaxDepth());


                    //System.out.println(2);

                while (linksMatcher.find()) {
                    if(workers.getStatus() == 0){
                        break;
                    }
                    String link = linksMatcher.group().replaceAll("href=[\"\']/?", "");

                    if (!link.matches("^https?://.+")) {
                        if (link.matches(".+\\.(?!html|php).*(\\/.+\\.(html|php)$)?")) {
                            link = "https:" + (link.matches("^/.*") ? "" : "/") + (link.matches("^//.*") ? "" : "/") + link;
                        } else if (!link.matches(url.replaceAll("\\/[^\\/]+$", ""))) {
                            link = url.replaceAll("\\/[^\\/]+$", "") + (url.matches(".*/$") ? "" : "/") + link;
                        }
                    }
                    System.out.println(depth + 1 + " - - " + link);

                    workers.getDataForCrawler().add(new MyData(depth + 1, link));

                }
                if(depth > 0 ) {
                    workers.getSaveUrlAndTitle().put(url, searchTitleLabel(webPage.getTheWebPageText()));
                    workers.parsedCPagesTextLabel.setText(String.valueOf(workers.parsedPages.incrementAndGet()));

                }

            }
        }
    }

    String searchTitleLabel(String webPage){
        Matcher matcher = Pattern.compile(
                "<title>.+</title>"
        ).matcher(webPage);

        if (matcher.find()) {
            return matcher.group().replaceAll("</?title>", "");
        } else {
            return "";
        }
    }

    @Override
    public void run() throws Flags {
            while (true){
                if(workers.getStatus() == 0){
                    System.out.println(this.getName() + " Stop");
                    break;
                }
                try {
                    MyData data = workers.getDataForCrawler().poll();
                    if(data != null){
                        workers.workersRunCount.incrementAndGet();
                        System.out.println(this.getName());
                        //System.out.println(workers.getMaxDepth());
                        if (workers.getMaxDepth() == -1 || data.getDepth() <= workers.getMaxDepth()) {
                            parse(data.getDepth(), data.getUrl());
                        }
                        workers.workersRunCount.decrementAndGet();
                        //System.out.println(workers.workersRunCount.decrementAndGet());
                    }else if(workers.workersRunCount.get() < 1){
                        workers.setStatus(0);
                        break;
                    }else {
                        Thread.sleep(500);
                    }
                }catch (IOException | NullPointerException | InterruptedException e){
                    e.printStackTrace();
                    workers.workersRunCount.decrementAndGet();
                }
            }

    }
}
