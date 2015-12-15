package com.cons.gps2exif.datamodel;

import java.util.Date;

/**
 * JpegのEXIF情報を格納するためのクラス
 * 
 * @author saito
 *
 */
public class ExifInfo {

  private String filePath = "";
  private Date updateDate;
  private Date photographicDate;
  private Float latitude = 0f;
  private Float longitude = 0f;
  private String maker = "";
  private String models = "";

  /**
   * @return filePath
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * @param filePath セットする filePath
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * @return updateDate
   */
  public Date getUpdateDate() {
    return updateDate;
  }

  /**
   * @param updateDate セットする updateDate
   */
  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  /**
   * @return photographicDate
   */
  public Date getPhotographicDate() {
    return photographicDate;
  }

  /**
   * @param photographicDate セットする photographicDate
   */
  public void setPhotographicDate(Date photographicDate) {
    this.photographicDate = photographicDate;
  }

  /**
   * @return photographicDate
   */
  public Date getRecordDate() {
    return photographicDate;
  }

  /**
   * @param photographicDate セットする photographicDate
   */
  public void setRecordDate(Date recordDate) {
    this.photographicDate = recordDate;
  }

  /**
   * @return latitude
   */
  public Float getLatitude() {
    return latitude;
  }

  /**
   * @param latitude セットする latitude
   */
  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  /**
   * @return longitude
   */
  public Float getLongitude() {
    return longitude;
  }

  /**
   * @param longitude セットする longitude
   */
  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }

  /**
   * @return maker
   */
  public String getMaker() {
    return maker;
  }

  /**
   * @param maker セットする maker
   */
  public void setMaker(String maker) {
    this.maker = maker;
  }

  /**
   * @return models
   */
  public String getModels() {
    return models;
  }

  /**
   * @param models セットする models
   */
  public void setModels(String models) {
    this.models = models;
  }

}
