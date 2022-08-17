package com.datarangers.sender;

/**
 * 发送之后的回调函数
 *
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2022-04-24
 */
public interface Callback {

  void onFailed(FailedData failedData);


  class FailedData {

    public FailedData(String message, String cause, boolean listable) {
      this(message, cause, null, listable);
    }

    public FailedData(String message, String cause, Exception exception, boolean listable) {
      this.message = message;
      this.cause = cause;
      this.exception = exception;
      this.listable = listable;
    }

    /**
     * 上报的报文内容
     */
    private String message;

    /**
     * 失败的原因，如果有的话
     */
    private String cause;

    /**
     * 失败时的异常，如果有的话
     */
    private Exception exception;

    private boolean listable;

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public String getCause() {
      return cause;
    }

    public void setCause(String cause) {
      this.cause = cause;
    }

    public Exception getException() {
      return exception;
    }

    public void setException(Exception exception) {
      this.exception = exception;
    }

    public boolean isListable() {
      return listable;
    }

    public void setListable(boolean listable) {
      this.listable = listable;
    }
  }
}
