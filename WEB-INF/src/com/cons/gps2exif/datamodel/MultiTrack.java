package com.cons.gps2exif.datamodel;

import java.util.ArrayList;

/**
 * google trackのgpsログデータを格納するためのクラス
 * 
 * @author saito
 *
 */
public class MultiTrack {

  private String altitudeMode = "";
  private String interpolate = "";

  private ArrayList<Track> tracks;


  /**
   * @return tracks
   */
  public ArrayList<Track> getTracks() {
    return tracks;
  }

  /**
   * @param tracks セットする tracks
   */
  public void setTracks(ArrayList<Track> tracks) {
    this.tracks = tracks;
  }

  /**
   * @return interpolate
   */
  public String getInterpolate() {
    return interpolate;
  }

  /**
   * @param interpolate セットする interpolate
   */
  public void setInterpolate(String interpolate) {
    this.interpolate = interpolate;
  }

  /**
   * @return altitudeMode
   */
  public String getAltitudeMode() {
    return altitudeMode;
  }

  /**
   * @param altitudeMode セットする altitudeMode
   */
  public void setAltitudeMode(String altitudeMode) {
    this.altitudeMode = altitudeMode;
  }

}
