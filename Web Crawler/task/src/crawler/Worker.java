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
        //System.out.println(url);

        TextWebPage webPage  = new TextWebPage();

        if (webPage.getWebPage(url) != null) {

            Matcher linksMatcher = Pattern.compile(
                    "href=[\"\'][A-Za-z%\\d:\\/.]+"
            ).matcher(webPage.getTheWebPageText());

            if(depth < workers.getMaxDepth()) {

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

                    workers.getUrls().add(new MyData(depth + 1, link));

                }
            }

            workers.getMap().put(url, searchTitleLabel(webPage.getTheWebPageText()));
            workers.parsedPages.incrementAndGet();

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
                        MyData data = workers.getUrls().poll();
                    if(data != null){
                        workers.workersRunCount.incrementAndGet();
                        //int depth = Integer.parseInt(str[0]);
                        //String url = str[1];
                        System.out.println(this.getName());
                        //System.out.println(workers.getMaxDepth());
                        if (workers.getMaxDepth() == -1 || data.getDepth() <= workers.getMaxDepth()) {
                            parse(data.getDepth(), data.getUrl());
                        }
                        //System.out.println(workers.workersRunCount.decrementAndGet());
                    }else if(workers.workersRunCount.get() < 1){
                        break;
                    }
                }catch (IOException | NullPointerException e){
                    e.printStackTrace();
                    workers.workersRunCount.decrementAndGet();
                }
            }

    }
}
