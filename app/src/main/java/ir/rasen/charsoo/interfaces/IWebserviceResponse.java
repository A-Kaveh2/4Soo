package ir.rasen.charsoo.interfaces;

/**
 * Created by android on 12/16/2014.
 */
public interface IWebserviceResponse {
  /*  void getResult(User result);

    void getResult(Business result);

    void getResult(ResultStatus result);

    void getResult(CommentNotification result);

    <T> void getResult(Collection<T> result);*/

    void getResult(Object result);



    void getError(Integer errorCode);

}
