package util.exception;


public class AuctionExistException extends Exception {


    public AuctionExistException() {
    }

   
    public AuctionExistException(String msg) {
        super(msg);
    }
}
