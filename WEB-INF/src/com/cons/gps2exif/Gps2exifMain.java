package com.cons.gps2exif;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.ParseException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import com.cons.gps2exif.datamodel.GPSTrack;
import com.cons.gps2exif.datatype.FileType;
import com.cons.gps2exif.datatype.TargetType;
import com.cons.gps2exif.gps.FileUtility;
import com.cons.gps2exif.gps.GpsFileReader;
import com.cons.gps2exif.message.MessageBuilder;
import com.cons.gps2exif.service.DataEmbedding;

public class Gps2exifMain {

  // KMZ(KML)ファイルの一覧
  private static ArrayList<File> targetFiles = new ArrayList<>();

  // JPG(JPEG)ファイルの一覧
  private static ArrayList<File> jpgFiles = new ArrayList<>();

  // 結果ファイルの出力先Directory
  private static File jpgFileDst = null;

  // 引数処理を行うためのArgumentOperationクラスのインスタンス生成
  private static ArgumentOperation arguOp = new ArgumentOperation();

  // 進捗表示などを行うUtilクラス
  private static ShowConsole showConsole = new ShowConsole();


  public static void main(String[] args) {

    // 開始メッセージ
    System.out.println(new Date() + " " + MessageBuilder.APP_STARTT);

    /**
     * 引数チェックを行う
     */
    try {
      arguOp.parseArg(args);
    } catch (ParseException e1) {
      // ここに来るのはバグ
      // 終了メッセージ
      System.out.println(new Date() + " " + MessageBuilder.APP_FINISH);
      return;
    }


    /**
     * gpsデータを読み込む
     */

    // 解析結果を格納するArrayListインスタンス
    ArrayList<GPSTrack> analysedData = new ArrayList<>();

    try {

      // GPSデータファイルの取得
      analysedData = readGpsLogFiles();

    } catch (IOException e) {
      // GPSファイルが読み込めない：指定されたディレクトリやファイルがない
      System.out.println(e.getMessage());
      System.out.println(new Date() + " " + MessageBuilder.APP_FINISH);
      return;

    } catch (XMLStreamException e) {
      // GPSファイルが読み込めない：不正な形式のgpsログ
      System.out.println(MessageBuilder.XML_PARSE_EXCEPTION);
      System.out.println(new Date() + " " + MessageBuilder.APP_FINISH);
      return;

    } catch (Exception e) {
      // 想定外のエラー：ここに来るのはバグ
      System.out.println(MessageBuilder.APP_Unexpected_FINISH);
      e.printStackTrace();
      System.out.println(new Date() + " " + MessageBuilder.APP_FINISH);
      return;
    }

    // GPSログの解析のみのオプションが与えられた場合は、画像関連の処理はしない
    if (!arguOp.isNoUpdateJpgFileFlag()) {

      /**
       * 出力先はディレクトリ名とする。そしてディレクトリがなければ作る。
       */
      jpgFileDst = new File(arguOp.getOUTPUT_JPG_PATH());


      /**
       * jpgファイルを読み込む
       */
      DataEmbedding dataSet = new DataEmbedding();

      // 進捗の表示
      if (arguOp.isShowProgressFlg()) {
        dataSet.setShowProgress(true);
      }

      try {

        // 書き込み対象画像の取得:対象はファイルとは限らずディレクトリかもしれない
        File targetJpgFiles = new File(arguOp.getINPUT_JPG_PATH());

        // ファイルの検索とディレクトリの作成
        readJpgFiles(targetJpgFiles);

        // 解析対象のファイルをセット
        dataSet.setTargetJpegFileList(jpgFiles);

        // ファイル編集時刻で並び替え
        dataSet.sortDataSet();

        // 出力先のディレクトリを設定
        dataSet.setOutputDir(jpgFileDst);

        // 写真の撮影時刻の補正量(単位：分)の指定
        dataSet.setTimeDiff(arguOp.getADJUST_PHOTO_TIME());

        // 解析方法の引数からの取得と設定
        dataSet.setMethodType(arguOp.getDecideMethod());

        // GPSデータと写真撮影日時の突合とGPS埋め込み
        dataSet.setGpsData(analysedData);

      } catch (IOException e) {
        // 画像ファイルの読み込みに失敗
        System.out.println(e.getMessage());
        e.printStackTrace();
        System.out.println(new Date() + " " + MessageBuilder.APP_FINISH);
        return;

      } catch (ImageReadException | ImageWriteException e) {
        // 画像ファイルの読み書きに失敗
        System.out.println(e.getMessage());
        System.out.println(MessageBuilder.READ_OR_WRTITE_IMAGE_ERROR + e.getMessage());
        e.printStackTrace();
        System.out.println(new Date() + " " + MessageBuilder.APP_FINISH);
        return;

      } finally {

        // 結果まとめの表示
        if (arguOp.isShowResultSummeryFlg()) {
          showConsole.showSummery(dataSet);
        }

      }
    }

    // 終了メッセージ
    System.out.println(MessageBuilder.APP_FINISH);

  }

  /**
   * @return
   * @throws IOException
   * @throws XMLStreamException
   * @throws Exception
   */
  private static ArrayList<GPSTrack> readGpsLogFiles() throws IOException, XMLStreamException {

    ArrayList<GPSTrack> analysedData = new ArrayList<GPSTrack>();

    // 解析対象pathを取得:対象はファイルとは限らずディレクトリかもしれない
    File targetGpsFiles = new File(arguOp.getINPUT_KMZ_PATH());

    // 解析対象ファイルを格納するインスタンス
    FileUtility fileUtil = new FileUtility();

    // ファイルの存在チェックとファイルかディレクトリかどうかを判定
    if (TargetType.File == fileUtil.isTargetType(targetGpsFiles)) {
      targetFiles.add(targetGpsFiles);
    } else if (TargetType.Directory == fileUtil.isTargetType(targetGpsFiles)) {

      // 指定されたディレクトリ以下にあるkmz/kmlを全てtargetFilesへ追加
      fileUtil.addSuffixStrs(FileType.KMZ.toString());
      fileUtil.addSuffixStrs(FileType.KML.toString());
      fileUtil.addSuffixStrs(FileType.GPX.toString());

      targetFiles.addAll(fileUtil.searchFolder(targetGpsFiles));

    } else if (TargetType.notExist == fileUtil.isTargetType(targetGpsFiles)) {
      throw new IOException(MessageBuilder.ARG_DIR_ERROR + targetGpsFiles.toString());
    }

    GpsFileReader parse = new GpsFileReader();

    // GPSファイルの時刻ずれを補正する量をセット
    parse.setTimeDiff(arguOp.getADJUST_GPS_TIME());

    for (File file : targetFiles) {
      analysedData.addAll(parse.gpsDecode(file));
    }

    if (analysedData.size() > 0) {
      // GPSデータのまとめの表示
      if (arguOp.isShowKmzSummeryFlg()) {
        showConsole.printGPSabstract(analysedData);
      }

      // 実際のGPSデータの全表示
      if (arguOp.isShowRawGpsDataFlag()) {
        showConsole.printTrackData(analysedData);
      }
    }

    // 参照の削除
    fileUtil = null;

    return analysedData;

  }


  /**
   * @param targetJpgFiles
   * @throws ImageReadException
   * @throws IOException
   */
  private static void readJpgFiles(File targetJpgFiles) throws ImageReadException, IOException {

    // TODO ファイル名によるフィルタ機能の追加

    // 解析対象ファイルを格納するインスタンス
    FileUtility fileUtil = new FileUtility();

    // ファイルの存在チェックとファイルかディレクトリかどうかを判定
    if (TargetType.File == fileUtil.isTargetType(targetJpgFiles)) {
      jpgFiles.add(targetJpgFiles);
    } else if (TargetType.Directory == fileUtil.isTargetType(targetJpgFiles)) {

      // 指定されたディレクトリ以下にあるkmz/kmlを全てjpgFilesへ追加
      fileUtil.addSuffixStrs(FileType.JPG.toString());
      fileUtil.addSuffixStrs(FileType.JPEG.toString());

      jpgFiles.addAll(fileUtil.searchFolder(targetJpgFiles));

    } else if (TargetType.notExist == fileUtil.isTargetType(targetJpgFiles)) {
      throw new IOException(MessageBuilder.ARG_DIR_ERROR + targetJpgFiles.toString());
    }

    if (TargetType.Directory != fileUtil.isTargetType(jpgFileDst)) {

      if (arguOp.isMakeDirFlg()) {
        // 指定された出力ディレクトリがなければ作る
        jpgFileDst.mkdirs();
      } else if (!jpgFileDst.exists()) {
        throw new ImageReadException(MessageBuilder.MAKING_DIR_FAILED);
      }

    }
  }

}
