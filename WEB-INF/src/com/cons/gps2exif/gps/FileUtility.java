package com.cons.gps2exif.gps;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.cons.gps2exif.datatype.TargetType;
import com.cons.gps2exif.message.MessageBuilder;

public class FileUtility {

  private Set<File> fileList = new HashSet<>();
  private Set<String> suffixStrs = new HashSet<>();

  public FileUtility() {}

  // コンストラクタ
  public FileUtility(Set<String> suffixStr) {
    if (!(suffixStr == null) && suffixStr.size() > 0) {
      this.suffixStrs = suffixStr;
    } else {
      suffixStr.clear();
    }
  }


  // ファイルかディレクトリかを判定
  public TargetType isTargetType(File file) {

    if (isFileExist(file)) {
      if (file.isFile()) {
        return TargetType.File;
      } else if (file.isDirectory()) {
        return TargetType.Directory;
      }
    }
    return TargetType.notExist;
  }

  /**
   * 引数で指定されたファイルが読み込める状態のjpg/jpegファイルであるかを判定する
   * 
   * @param fileName
   * @return 拡張子
   */
  // public static boolean isJpgFile(File file) {
  //
  // // ファイルの存在チェック
  // isFileExist(file);
  //
  // String filePath = file.getPath().toLowerCase();
  //
  // if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
  // return true;
  // } else {
  // System.out.print(MessageBuilder.JPG_EXT_INCORRECT_ERROR);
  // return false;
  // }
  //
  // }

  /**
   * 引数で指定されたファイルが読み込める状態のkmz/kmlファイルであるかを判定する
   * 
   * @param fileName
   * @return 拡張子
   * @throws Exception
   */
  public static String getFileSuffix(File file) throws IOException {

    // ファイルの存在チェック
    if (isFileExist(file)) {

      String filePath = file.getPath().toLowerCase();

      int lastDotPosition = filePath.lastIndexOf(".");
      if (lastDotPosition != -1) {
        return filePath.substring(lastDotPosition + 1);
      } else {
        System.out.print(MessageBuilder.GPS_EXT_INCORRECT_ERROR);
        throw new IOException();
      }

    } else {
      System.out.print(MessageBuilder.GPS_EXT_INCORRECT_ERROR);
      throw new IOException();
    }

  }

  /**
   * ファイルが存在を確認するメソッド
   * 
   * @param fileName
   * @return 拡張子
   */
  private static boolean isFileExist(File file) {

    if (file == null) {
      System.out.print("引数が誤り");
      return false;
    }

    if (!file.exists()) {
      return false;
    }

    return true;
  }



  /**
   * ディレクトリを再帰的に読む
   * 
   * @param folderPath
   */
  public Set<File> searchFolder(File dir) {

    File[] files = dir.listFiles();

    if (files == null) {
      fileList.clear();
      return fileList;
    } else {
      for (File file : files) {
        if (!file.exists())
          continue;
        else if (file.isDirectory())
          searchFolder(file);
        else if (file.isFile())
          execute(file);
      }
    }
    return fileList;
  }

  /**
   * ファイルの処理
   * 
   * @param filePath
   */
  private void execute(File file) {
    String pathStr = file.getAbsoluteFile().toString().toLowerCase();
    for (String suffix : suffixStrs) {

      if (pathStr.endsWith(suffix.toLowerCase())) {
        fileList.add(file);
        break;
      }
    }
  }

  /**
   * @param suffixStrs セットする suffixStrs
   */
  public void setSuffixStrs(Set<String> suffixStrs) {
    this.suffixStrs = suffixStrs;
  }

  public void addSuffixStrs(String suffix) {
    this.suffixStrs.add(suffix);
  }

  public void removeSuffixStrs(String suffix) {
    this.suffixStrs.remove(suffix);
  }

}
