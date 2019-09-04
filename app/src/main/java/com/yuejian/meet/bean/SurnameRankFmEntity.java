package com.yuejian.meet.bean;

/**
 * @author :
 * @time : 2018/11/19 14:56
 * @desc :
 * @version: V1.0
 * @update : 2018/11/19 14:56
 */

public class SurnameRankFmEntity {


  /**
   * update_time : 1541401471000
   * pinyin : Lǐ
   * surname : 李
   * practice_total : 87210
   * rank : 1
   * id : 180
   */

  private long update_time;
  private String pinyin;
  private String surname;
  private int practice_total;
  private int rank;
  private int id;

  public long getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(long update_time) {
    this.update_time = update_time;
  }

  public String getPinyin() {
    return pinyin;
  }

  public void setPinyin(String pinyin) {
    this.pinyin = pinyin;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public int getPractice_total() {
    return practice_total;
  }

  public void setPractice_total(int practice_total) {
    this.practice_total = practice_total;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
