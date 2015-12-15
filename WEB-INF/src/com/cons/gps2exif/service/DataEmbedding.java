package com.cons.gps2exif.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import com.cons.gps2exif.datamodel.GPSTrack;
import com.cons.gps2exif.datatype.DeterminationMethodType;
import com.cons.gps2exif.exif.EditEXIF;
import com.cons.gps2exif.exif.ReadMetaData;
import com.cons.gps2exif.message.MessageBuilder;


public class DataEmbedding {

  /**
   * 解析対象のファイルリスト
   */
  private ArrayList<File> targetJpegFileList = new ArrayList<File>();


  /**
   * 出力先ディレクトリ
   */
  private File outputDir = null;

  /**
   * 撮影時刻の補正量(分)
   */
  private Integer timeDiff = 0;

  /**
   * 進捗表示の要否
   */
  private boolean showProgress = false;

  // 正常処理したファイルの一覧
  private ArrayList<File> completeFiles = new ArrayList<>();

  // gpsデータ範囲外できなかったファイルの一覧
  private ArrayList<File> outOfRangeFiles = new ArrayList<>();

  // 異常発生したファイルの一覧
  private ArrayList<File> errorOccuredFiles = new ArrayList<>();

  // 位置情報の決定方法
  private DeterminationMethodType methodType = DeterminationMethodType.None;

  /**
   * @param showProgress セットする showProgress
   */
  public void setShowProgress(boolean showProgress) {
    this.showProgress = showProgress;
  }

  /**
   * @return completeFiles
   */
  public ArrayList<File> getCompleteFiles() {
    return completeFiles;
  }

  /**
   * @return outOfRangeFiles
   */
  public ArrayList<File> getOutOfRangeFiles() {
    return outOfRangeFiles;
  }

  /**
   * @return errorOccuredFiles
   */
  public ArrayList<File> getErrorOccuredFiles() {
    return errorOccuredFiles;
  }

  /**
   * @return targetJpegFileList
   */
  public ArrayList<File> getTargetJpegFileList() {
    return targetJpegFileList;
  }

  /**
   * @param targetJpegFileList セットする targetJpegFileList
   */
  public void setTargetJpegFileList(ArrayList<File> targetJpegFileList) {
    this.targetJpegFileList = targetJpegFileList;
  }


  /**
   * @param outputDir セットする outputDir
   */
  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  /**
   * @param timeDiff セットする timeDiff
   */
  public void setTimeDiff(Integer timeDiff) {
    this.timeDiff = timeDiff;
  }

  /**
   * @param methodType セットする methodType
   */
  public void setMethodType(DeterminationMethodType methodType) {
    this.methodType = methodType;
  }

  /**
   * 指定された時刻photographyDateが、引数で渡されたanalysedDataで取得された日時の範囲内にあるかどうかを判定
   * 
   * @param photographyDate
   * @param analysedData
   * @return
   * @throws ImageReadException
   * @throws IOException
   * @throws ImageWriteException
   */
  public void setGpsData(ArrayList<GPSTrack> analysedData) throws ImageReadException, IOException, ImageWriteException {

    ReadMetaData meta = new ReadMetaData();
    EditEXIF editExif = new EditEXIF();

    for (File targjpgFile : targetJpegFileList) {

      boolean progressed = false;

      // 進捗表示の要否
      if (showProgress) {
        System.out.println(targjpgFile.getAbsolutePath());
      }

      try {

        Date photographyDate = meta.getPhotographyDate(targjpgFile);
        if (null == photographyDate) {
          // もし画像から撮影時刻を取得できなかった場合はスキップする
          continue;
        }

        // 撮影時刻の補正
        if (0 != timeDiff) {
          // 補正処理のためCalendarを生成
          Calendar cal = Calendar.getInstance();
          cal.setTime(photographyDate);
          cal.add(Calendar.MINUTE, timeDiff);

          // 補正処理後の時刻をセット
          photographyDate = cal.getTime();
        }


        // 入力されたgpsデータのデータ範囲ないかどうかを判定
        roop: for (GPSTrack gpsTrack : analysedData) {

          Date firstDate = gpsTrack.getRecordDate().get(0);
          Date lastDate = gpsTrack.getRecordDate().get(gpsTrack.getRecordDate().size() - 1);

          if (photographyDate.before(firstDate) || photographyDate.after(lastDate)) {
            // もし範囲外にある場合、このgpsTrackファイルの処理を中断
            continue;
          }

          // GPSログの中から撮影時刻の検索
          int i = 0;
          for (Date gpsDate : gpsTrack.getRecordDate()) {

            if (photographyDate.before(gpsDate)) {
              // 処理したフラグ
              progressed = true;

              Float pos[] = {0f, 0f,};
              // pos = setGps2Jpg(gpsDate, photographyDate, i);
              pos = setGps2Jpg(gpsTrack, photographyDate, i);

              editExif.setExifGPSTag(targjpgFile, outputDir, pos[0], pos[1]);

              break roop;
            }
            i++;
          }

        }

      } catch (IOException e) {
        // ファイルの読み書きに失敗した場合、エラーリストに追加
        errorOccuredFiles.add(targjpgFile);
        throw new IOException(MessageBuilder.ARG_DIR_ERROR + targjpgFile.toString());

      } catch (ImageWriteException e) {

        // ファイルの読み書きに失敗した場合、エラーリストに追加
        errorOccuredFiles.add(targjpgFile);
        throw new ImageWriteException(targjpgFile.toString());

      } catch (ImageReadException e) {

        // ファイルの読み書きに失敗した場合、エラーリストに追加
        errorOccuredFiles.add(targjpgFile);
        throw new ImageReadException(targjpgFile.toString());

      }

      if (progressed) {
        completeFiles.add(targjpgFile);
      } else {
        // スキップリストに追加
        outOfRangeFiles.add(targjpgFile);
      }

    }

  }

  // 画像ファイルの撮影時刻でソート
  public void sortDataSet() {

    ReadMetaData meta = new ReadMetaData();
    Map<Date, File> map = new HashMap<Date, File>();

    // ～要素を追加（put）とか～
    for (File jpgFile : targetJpegFileList) {
      Date photographyDate;

      try {
        photographyDate = meta.getPhotographyDate(jpgFile);

        if (null != photographyDate) {
          // もし画像から撮影時刻を取得できなかった場合はスキップする

          // 撮影時刻の補正
          if (0 != timeDiff) {
            // 補正処理のためCalendarを生成
            Calendar cal = Calendar.getInstance();
            cal.setTime(photographyDate);
            cal.add(Calendar.MINUTE, timeDiff);

            // 補正処理後の時刻をセット
            photographyDate = cal.getTime();
          }

          // 補正した撮影時刻とファイルのセットを格納
          map.put(photographyDate, jpgFile);

        }

      } catch (ImageReadException e) {
        // スキップリストに追加
        outOfRangeFiles.add(jpgFile);
      } catch (IOException e) {
        // スキップリストに追加
        outOfRangeFiles.add(jpgFile);
      }

    }

    // ソート
    Map<Date, File> ascSortedMap = new TreeMap<Date, File>();
    ascSortedMap.putAll(map);

    ArrayList<File> list = new ArrayList<File>();

    Iterator<Date> it = ascSortedMap.keySet().iterator();
    while (it.hasNext()) {
      list.add(ascSortedMap.get(it.next()));
    }

    // コピー前にクリア
    targetJpegFileList.clear();

    // 並び替え後のリストを差し替え
    targetJpegFileList.addAll(list);

  }

  // FIXME 修正中
  private Float[] setGps2Jpg(GPSTrack gpsTrack, Date photographyDate, int i) throws IOException {

    // GPSトラックの緯度経度のリストを取得
    ArrayList<Float> lonList = gpsTrack.getLongitude();
    ArrayList<Float> latList = gpsTrack.getLatitude();
    ArrayList<Date> gpsDateList = gpsTrack.getRecordDate();

    Float pos[] = {0f, 0f};

    if (null == lonList || null == latList || lonList.isEmpty() || latList.isEmpty()) {
      throw new IOException("lonList or latList is null or Zero size.");
    }

    // iの座標を返す
    pos[0] = lonList.get(i);
    pos[1] = latList.get(i);

    // iとi-1の近い方を返す
    Calendar photoTime = Calendar.getInstance();
    photoTime.setTime(photographyDate);
    long pTimeInLong = photoTime.getTimeInMillis();

    Calendar gpsTime0 = Calendar.getInstance();
    gpsTime0.setTime(gpsDateList.get(i - 1));
    long g0TimeInLong = gpsTime0.getTimeInMillis();

    Calendar gpsTime1 = Calendar.getInstance();
    gpsTime1.setTime(gpsDateList.get(i));
    long g1TimeInLong = gpsTime1.getTimeInMillis();

    if (DeterminationMethodType.NearBy == methodType) {
      // GPSの近いポイントで決める時
      if (g1TimeInLong - pTimeInLong > pTimeInLong - g0TimeInLong) {
        pos[0] = lonList.get(i - 1);
        pos[1] = latList.get(i - 1);
      }

    } else if (DeterminationMethodType.Interpolation == methodType) {
      // 補間でGPS座標を決める時
      float denominator = g1TimeInLong - g0TimeInLong;
      if (denominator > 0) {
        float ratio = (pTimeInLong - g0TimeInLong) / denominator;
        pos[0] = lonList.get(i - 1) + ratio * (lonList.get(i) - (lonList.get(i - 1)));
        pos[1] = latList.get(i - 1) + ratio * (latList.get(i) - (latList.get(i - 1)));
      }
    }

    return pos;

  }

  private void getTargetTime() {
    // GPSデータのarraylist時刻一覧から、画像の撮影時刻に対して最も近い時刻を求める


  }

  private void calcCoordinate() {
    // 画像の撮影時刻の位置情報を算出する


  }


}
