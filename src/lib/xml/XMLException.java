package lib.xml;

public class XMLException extends Exception
{
  public final static int ILLEGAL_ATTRIBUTE = 1;
  public final static int CLOSE_TAG_BEFORE_OPEN = 2;
  public final static int NO_CLOSE_TAG = 3;

  private int ErrorCode = 0;
  private Exception CauseException = null;

  public XMLException(String _MsgText, int _ErrorCode, Exception _CauseException)
  {
    super(_MsgText);
    ErrorCode = _ErrorCode;
    CauseException = _CauseException;
  }

  public Exception getCauseException()
  {
    return CauseException;
  }

  public int getErrorCode()
  {
    return ErrorCode;
  }
}
