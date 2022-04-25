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

    public FailedData(String message, String cause) {
      this(message, cause, null);
    }

    public FailedData(String message, String cause, Exception exception) {
      this.message = message;
      this.cause = cause;
      this.exception = exception;
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
  }
}
