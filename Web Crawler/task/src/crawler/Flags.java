package crawler;

public class Flags extends RuntimeException {
    final static public int MAXIMUM_DEPTH= 1;
    final static public int STOP_WORKER= 2;
    //final static public int LONG_LOGIN = 2;
    private int code;
    Flags(int code){
        this.code = code;
    }
    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
}
