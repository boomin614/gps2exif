package com.cons.gps2exif.datamodel;

import java.util.ArrayList;
import java.util.Date;

/**
 * google trackのgpsログデータを格納するためのクラス
 * 
 * @author saito
 *
 */
public class PlaceMark {

  private String id = "";
  private String name = "";
  private String styleurl = "";
  private String trackType = "";
  private String description = "";
  private Float latitude = 0f;
  private Float longitude = 0f;
  private Float altitude = 0f;
  private Date recordDate;
  private MultiTrack multiTrack;

  // FIXME 削除予定
  @Deprecated
  private ArrayList<Track> gpsTracks;


  /**
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id セットする id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name セットする name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description セットする description
   */
  public void setDescription(String description) {
    this.description = description;
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
   * @return altitude
   */
  public Float getAltitude() {
    return altitude;
  }

  /**
   * @param altitude セットする altitude
   */
  public void setAltitude(Float altitude) {
    this.altitude = altitude;
  }

  /**
   * @return recordDate
   */
  public Date getRecordDate() {
    return recordDate;
  }

  /**
   * @param recordDate セットする recordDate
   */
  public void setRecordDate(Date recordDate) {
    this.recordDate = recordDate;
  }

  /**
   * @return gpsTracks
   */
  // FIXME 削除予定
  @Deprecated
  public ArrayList<Track> getGpsTracks() {
    return gpsTracks;
  }

  /**
   * @param gpsTracks セットする gpsTracks
   */
  // FIXME 削除予定
  @Deprecated
  public void setGpsTracks(ArrayList<Track> gpsTracks) {
    this.gpsTracks = gpsTracks;
  }

  /**
   * @return styleurl
   */
  public String getStyleurl() {
    return styleurl;
  }

  /**
   * @param styleurl セットする styleurl
   */
  public void setStyleurl(String styleurl) {
    this.styleurl = styleurl;
  }

  /**
   * @return trackType
   */
  public String getTrackType() {
    return trackType;
  }

  /**
   * @param trackType セットする trackType
   */
  public void setTrackType(String trackType) {
    this.trackType = trackType;
  }

  /**
   * @return multiTrack
   */
  public MultiTrack getMultiTrack() {
    return multiTrack;
  }

  /**
   * @param multiTrack セットする multiTrack
   */
  public void setMultiTrack(MultiTrack multiTrack) {
    this.multiTrack = multiTrack;
  }



}
