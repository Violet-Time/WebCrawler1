package crawler;


import java.io.IOException;
import java.util.Scanner;

public class ApplicationRunner {
    public static void main(String[] args) throws IOException {
        WebCrawler webCrawler = new WebCrawler();
        webCrawler.setVisible(true);
    }
}
