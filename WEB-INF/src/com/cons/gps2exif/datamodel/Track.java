package com.cons.gps2exif.datamodel;

import java.util.ArrayList;

/**
 * google trackのkml形式におけるTrack要素を格納するためのクラス
 * 
 * @author saito
 *
 */
public class Track {

  private ArrayList<String> whens = new ArrayList<String>();
  private ArrayList<String> coords = new ArrayList<String>();
  private ArrayList<String> speeds = new ArrayList<String>();
  private ArrayList<String> bearings = new ArrayList<String>();
  private ArrayList<String> accuracies = new ArrayList<String>();

  /**
   * @return whens
   */
  public ArrayList<String> getWhens() {
    return whens;
  }

  /**
   * @param whens セットする whens
   */
  public void setWhens(ArrayList<String> whens) {
    this.whens = whens;
  }

  public void addWhens(String when) {
    this.whens.add(when);
  }

  public void removeWhen(String when) {
    this.whens.remove(when);
  }

  /**
   * @return coords
   */
  public ArrayList<String> getCoords() {
    return coords;
  }

  /**
   * @param coords セットする coords
   */
  public void setCoords(ArrayList<String> coords) {
    this.coords = coords;
  }

  public void addCoord(String coord) {
    this.coords.add(coord);
  }

  public void removeCoord(String coord) {
    this.coords.remove(coord);
  }

  /**
   * @return speeds
   */
  public ArrayList<String> getSpeeds() {
    return speeds;
  }

  /**
   * @param speeds セットする speeds
   */
  public void setSpeeds(ArrayList<String> speeds) {
    this.speeds = speeds;
  }

  public void addSpeed(String speed) {
    this.speeds.add(speed);
  }

  public void removeSpeed(String speed) {
    this.speeds.remove(speed);
  }

  /**
   * @return bearings
   */
  public ArrayList<String> getBearings() {
    return bearings;
  }

  /**
   * @param bearings セットする bearings
   */
  public void setBearings(ArrayList<String> bearings) {
    this.bearings = bearings;
  }

  public void addBearing(String bearing) {
    this.bearings.add(bearing);
  }

  public void removeBearing(String bearing) {
    this.bearings.remove(bearing);
  }

  /**
   * @return accuracies
   */
  public ArrayList<String> getAccuracies() {
    return accuracies;
  }

  /**
   * @param accuracies セットする accuracies
   */
  public void setAccuracies(ArrayList<String> accuracies) {
    this.accuracies = accuracies;
  }

  public void addAccuracy(String accuracy) {
    this.accuracies.add(accuracy);
  }

  public void removeAccuracy(String accuracy) {
    this.accuracies.remove(accuracy);
  }
}
