package com.yuejian.meet.bean;

/**
 * @author :
 * @time : 2018/11/17 15:07
 * @desc :
 * @version: V1.0
 * @update : 2018/11/17 15:07
 */

public class QuestRewardsEntity {

  /**
   * signinMap : {"content":"每日签到获取功德，连续签到奖励更多","finishNumber":0,"isDo":0,"rewardsValue":100,"targetNumber":0,"title":"每日签到"}
   * referreMap : {"content":"推荐好友注册1人","finishNumber":0,"isDo":0,"rewardsValue":1000,"targetNumber":1,"title":"推荐好友"}
   * addFriendMap : {"content":"添加5名好友","finishNumber":0,"isDo":0,"rewardsValue":50,"targetNumber":5,"title":"添加好友"}
   * everyDayPracticeMap : {"content":"前往完成修行任意一项","finishNumber":3,"isDo":1,"rewardsValue":100,"targetNumber":1,"title":"每日修行"}
   */

  private SigninMapBean signinMap;
  private ReferreMapBean referreMap;
  private AddFriendMapBean addFriendMap;
  private EveryDayPracticeMapBean everyDayPracticeMap;

  public SigninMapBean getSigninMap() {
    return signinMap;
  }

  public void setSigninMap(SigninMapBean signinMap) {
    this.signinMap = signinMap;
  }

  public ReferreMapBean getReferreMap() {
    return referreMap;
  }

  public void setReferreMap(ReferreMapBean referreMap) {
    this.referreMap = referreMap;
  }

  public AddFriendMapBean getAddFriendMap() {
    return addFriendMap;
  }

  public void setAddFriendMap(AddFriendMapBean addFriendMap) {
    this.addFriendMap = addFriendMap;
  }

  public EveryDayPracticeMapBean getEveryDayPracticeMap() {
    return everyDayPracticeMap;
  }

  public void setEveryDayPracticeMap(EveryDayPracticeMapBean everyDayPracticeMap) {
    this.everyDayPracticeMap = everyDayPracticeMap;
  }

  public static class SigninMapBean {
    /**
     * content : 每日签到获取功德，连续签到奖励更多
     * finishNumber : 0
     * isDo : 0
     * rewardsValue : 100
     * targetNumber : 0
     * title : 每日签到
     */
    private int isGet;

    public int getIsGet() {
      return isGet;
    }

    public void setIsGet(int isGet) {
      this.isGet = isGet;
    }

    private String content;
    private int finishNumber;
    private int isDo;
    private int rewardsValue;
    private int targetNumber;
    private String title;

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public int getFinishNumber() {
      return finishNumber;
    }

    public void setFinishNumber(int finishNumber) {
      this.finishNumber = finishNumber;
    }

    public int getIsDo() {
      return isDo;
    }

    public void setIsDo(int isDo) {
      this.isDo = isDo;
    }

    public int getRewardsValue() {
      return rewardsValue;
    }

    public void setRewardsValue(int rewardsValue) {
      this.rewardsValue = rewardsValue;
    }

    public int getTargetNumber() {
      return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
      this.targetNumber = targetNumber;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }

  public static class ReferreMapBean {
    /**
     * content : 推荐好友注册1人
     * finishNumber : 0
     * isDo : 0
     * rewardsValue : 1000
     * targetNumber : 1
     * title : 推荐好友
     */

    private String content;
    private int finishNumber;
    private int isDo;
    private int rewardsValue;
    private int targetNumber;
    private String title;
    private int isGet;

    public int getIsGet() {
      return isGet;
    }

    public void setIsGet(int isGet) {
      this.isGet = isGet;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public int getFinishNumber() {
      return finishNumber;
    }

    public void setFinishNumber(int finishNumber) {
      this.finishNumber = finishNumber;
    }

    public int getIsDo() {
      return isDo;
    }

    public void setIsDo(int isDo) {
      this.isDo = isDo;
    }

    public int getRewardsValue() {
      return rewardsValue;
    }

    public void setRewardsValue(int rewardsValue) {
      this.rewardsValue = rewardsValue;
    }

    public int getTargetNumber() {
      return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
      this.targetNumber = targetNumber;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }

  public static class AddFriendMapBean {
    /**
     * content : 添加5名好友
     * finishNumber : 0
     * isDo : 0
     * rewardsValue : 50
     * targetNumber : 5
     * title : 添加好友
     */
    private int isGet;

    public int getIsGet() {
      return isGet;
    }

    public void setIsGet(int isGet) {
      this.isGet = isGet;
    }

    private String content;
    private int finishNumber;
    private int isDo;
    private int rewardsValue;
    private int targetNumber;
    private String title;

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public int getFinishNumber() {
      return finishNumber;
    }

    public void setFinishNumber(int finishNumber) {
      this.finishNumber = finishNumber;
    }

    public int getIsDo() {
      return isDo;
    }

    public void setIsDo(int isDo) {
      this.isDo = isDo;
    }

    public int getRewardsValue() {
      return rewardsValue;
    }

    public void setRewardsValue(int rewardsValue) {
      this.rewardsValue = rewardsValue;
    }

    public int getTargetNumber() {
      return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
      this.targetNumber = targetNumber;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }

  public static class EveryDayPracticeMapBean {
    /**
     * content : 前往完成修行任意一项
     * finishNumber : 3
     * isDo : 1
     * rewardsValue : 100
     * targetNumber : 1
     * title : 每日修行
     */

    private String content;
    private int finishNumber;
    private int isDo;
    private int rewardsValue;
    private int targetNumber;
    private String title;
    private int isGet;

    public int getIsGet() {
      return isGet;
    }

    public void setIsGet(int isGet) {
      this.isGet = isGet;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public int getFinishNumber() {
      return finishNumber;
    }

    public void setFinishNumber(int finishNumber) {
      this.finishNumber = finishNumber;
    }

    public int getIsDo() {
      return isDo;
    }

    public void setIsDo(int isDo) {
      this.isDo = isDo;
    }

    public int getRewardsValue() {
      return rewardsValue;
    }

    public void setRewardsValue(int rewardsValue) {
      this.rewardsValue = rewardsValue;
    }

    public int getTargetNumber() {
      return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
      this.targetNumber = targetNumber;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }
}
