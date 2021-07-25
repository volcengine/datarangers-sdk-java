package com.datarangers.message.saas;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-22
 */
public
class Attribute {

  private String name;
  private Object value;
  private String operation;

  public Attribute() {
  }

  public Attribute(String name, Object value, String operation) {
    this.name = name;
    this.value = value;
    this.operation = operation;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }
}

