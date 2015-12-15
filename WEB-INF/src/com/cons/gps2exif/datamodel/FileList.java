package com.cons.gps2exif.datamodel;

import java.io.File;
import java.util.ArrayList;

/**
 * JpegのEXIF情報を格納するためのクラス
 * 
 * @author saito
 *
 */
public class FileList {

  private ArrayList<File> jpgFiles = new ArrayList<File>();
  private ArrayList<File> kmlFiles = new ArrayList<File>();


  /**
   * @return jpgFiles
   */
  public ArrayList<File> getJpgFiles() {
    return jpgFiles;
  }

  /**
   * @param jpgFiles セットする jpgFiles
   */
  public void setJpgFiles(ArrayList<File> jpgFiles) {
    this.jpgFiles = jpgFiles;
  }

  /**
   * @return kmlFiles
   */
  public ArrayList<File> getKmlFiles() {
    return kmlFiles;
  }

  /**
   * @param kmlFiles セットする kmlFiles
   */
  public void setKmlFiles(ArrayList<File> kmlFiles) {
    this.kmlFiles = kmlFiles;
  }

}
