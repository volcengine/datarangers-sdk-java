package com.datarangers.config;

import com.datarangers.util.Tools;

/**
 * @Author zhangpeng.spin@bytedance.com
 * @Date 2021-07-23
 */
public class OpenapiConfig {
  private String domain;

  private String ak;
  private String sk;

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getAk() {
    return ak;
  }

  public void setAk(String ak) {
    this.ak = ak;
  }

  public String getSk() {
    return sk;
  }

  public void setSk(String sk) {
    this.sk = sk;
  }

  @Override
  public String toString(){
    return String.format("{\"domain\": %s, \"ak\"=\"%s\", \"sk\"=\"%s\"}", domain, Tools.passwordMask(ak), Tools.passwordMask(sk));
  }
}
