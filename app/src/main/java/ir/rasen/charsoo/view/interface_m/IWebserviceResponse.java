package ir.rasen.charsoo.view.interface_m;

/**
 * Created by android on 12/16/2014.
 */
public interface IWebserviceResponse {
    void getResult(Object result);
    void getError(Integer errorCode,String callerStringID);
}
