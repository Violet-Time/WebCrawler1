package crawler;

public class MyData {
    private int depth;
    private String url;

    MyData(int depth, String url){
        this.depth = depth;
        this.url = url;
    }

    public int getDepth() {
        return depth;
    }

    public String getUrl() {
        return url;
    }
}
