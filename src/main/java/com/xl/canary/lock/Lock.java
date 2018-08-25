package com.xl.canary.lock;

import java.util.Date;

/**
 * 全局锁，包括锁的名称
 * @author jyou
 */
public class Lock {

  private String name;
  private String value;

  public Lock(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

}

