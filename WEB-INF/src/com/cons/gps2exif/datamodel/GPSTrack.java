package com.cons.gps2exif.datamodel;

import java.util.ArrayList;
import java.util.Date;

/**
 * KML形式のTrack形式のデータを処理しやすいようにしたgpsデータの保持のためのデータモデル
 * 
 * @author saito
 *
 */
public class GPSTrack {

  // track名。placemarkで定義されてるはず
  private String dataTitle = "";

  // ファイルパス名でもいいかも
  private String filePath = "";

  private ArrayList<Date> recordDate = new ArrayList<>();
  private ArrayList<Float> latitude = new ArrayList<>();
  private ArrayList<Float> longitude = new ArrayList<>();
  private ArrayList<Float> altitude = new ArrayList<>();
  private ArrayList<Float> speed = new ArrayList<>();
  private ArrayList<Float> bearing = new ArrayList<>();
  private ArrayList<Float> accuracy = new ArrayList<>();

  /**
   * @return dataTitle
   */
  public String getDataTitle() {
    return dataTitle;
  }

  /**
   * @param dataTitle セットする dataTitle
   */
  public void setDataTitle(String dataTitle) {
    this.dataTitle = dataTitle;
  }

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
   * @return recordDate
   */
  public ArrayList<Date> getRecordDate() {
    return recordDate;
  }

  /**
   * @param recordDate セットする recordDate
   */
  public void setRecordDate(ArrayList<Date> recordDate) {
    this.recordDate = recordDate;
  }

  /**
   * @return latitude
   */
  public ArrayList<Float> getLatitude() {
    return latitude;
  }

  /**
   * @param latitude セットする latitude
   */
  public void setLatitude(ArrayList<Float> latitude) {
    this.latitude = latitude;
  }

  /**
   * @return longitude
   */
  public ArrayList<Float> getLongitude() {
    return longitude;
  }

  /**
   * @param longitude セットする longitude
   */
  public void setLongitude(ArrayList<Float> longitude) {
    this.longitude = longitude;
  }

  /**
   * @return altitude
   */
  public ArrayList<Float> getAltitude() {
    return altitude;
  }

  /**
   * @param altitude セットする altitude
   */
  public void setAltitude(ArrayList<Float> altitude) {
    this.altitude = altitude;
  }

  /**
   * @return speed
   */
  public ArrayList<Float> getSpeed() {
    return speed;
  }

  /**
   * @param speed セットする speed
   */
  public void setSpeed(ArrayList<Float> speed) {
    this.speed = speed;
  }

  /**
   * @return bearing
   */
  public ArrayList<Float> getBearing() {
    return bearing;
  }

  /**
   * @param bearing セットする bearing
   */
  public void setBearing(ArrayList<Float> bearing) {
    this.bearing = bearing;
  }

  /**
   * @return accuracy
   */
  public ArrayList<Float> getAccuracy() {
    return accuracy;
  }

  /**
   * @param accuracy セットする accuracy
   */
  public void setAccuracy(ArrayList<Float> accuracy) {
    this.accuracy = accuracy;
  }

}
