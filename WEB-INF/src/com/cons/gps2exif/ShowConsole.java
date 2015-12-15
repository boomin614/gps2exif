package com.cons.gps2exif;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.cons.gps2exif.datamodel.GPSTrack;
import com.cons.gps2exif.datamodel.PlaceMark;
import com.cons.gps2exif.service.DataEmbedding;

public class ShowConsole {

  private final String DATA_DELIMINATOR = "\t";
  private final String LINE_SEPARATOR = "\n";

  /**
   * @param trackData
   */
  protected void printTrackData(ArrayList<GPSTrack> trackData) {

    if (trackData != null && trackData.size() > 0) {

      for (GPSTrack track : trackData) {

        System.out.println("path  : " + track.getFilePath());

        System.out.print("date      " + DATA_DELIMINATOR);
        System.out.print("time    " + DATA_DELIMINATOR);
        System.out.print("lon     " + DATA_DELIMINATOR);
        System.out.print("lat     " + DATA_DELIMINATOR);
        System.out.print("alti" + DATA_DELIMINATOR);
        System.out.print("speed" + DATA_DELIMINATOR);
        System.out.print("bearing" + DATA_DELIMINATOR);
        System.out.print("accuracy" + LINE_SEPARATOR);


        int gpsDataSize = track.getRecordDate().size();
        for (int i = 0; i < gpsDataSize; i++) {

          System.out.print(DateFormatUtils.ISO_DATE_FORMAT.format(track.getRecordDate().get(i)) + DATA_DELIMINATOR);
          System.out.print(DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(track.getRecordDate().get(i)) + DATA_DELIMINATOR);

          System.out.printf("%3.5f" + DATA_DELIMINATOR, track.getLongitude().get(i));
          System.out.printf("%2.5f" + DATA_DELIMINATOR, track.getLatitude().get(i));

          System.out.printf("%.2f" + DATA_DELIMINATOR, track.getAltitude().get(i));
          System.out.printf("%.2f" + DATA_DELIMINATOR, track.getSpeed().get(i));
          System.out.printf("%.2f" + DATA_DELIMINATOR, track.getBearing().get(i));
          System.out.printf("%.2f" + LINE_SEPARATOR, track.getAccuracy().get(i));

        }
      }

    } else {
      System.out.println("track data is null or no data.");
    }

  }

  /**
   * @param placeMarks
   */
  protected void printPlaceMark(ArrayList<PlaceMark> placeMarks) {

    System.out.println("PlaceMark data: ");

    if (placeMarks != null && placeMarks.size() > 0) {

      for (PlaceMark placeMark : placeMarks) {

        System.out.println("id: " + placeMark.getId());
        System.out.println("styleurl: " + placeMark.getStyleurl());
        System.out.println("trackType: " + placeMark.getTrackType());
        System.out.println("name: " + placeMark.getName());
        if (placeMark.getTrackType() == null || placeMark.getTrackType().isEmpty()) {
          System.out.println("date: " + placeMark.getRecordDate());
          System.out.println("desc: " + placeMark.getDescription());
          System.out.println("long: " + placeMark.getLongitude());
          System.out.println("lati: " + placeMark.getLatitude());
          System.out.println("alt: " + placeMark.getAltitude());
        }
        System.out.println("");
      }

    } else {
      System.out.println("PlaceMark data is null or no data.");
    }

  }

  protected void printGPSabstract(ArrayList<GPSTrack> analysedData) {

    System.out.println("\nNum of log files: " + analysedData.size());

    for (GPSTrack trackData : analysedData) {

      int datanum = trackData.getRecordDate().size();
      // 結果表示2：trackdataの数
      System.out.println("\tpath  : " + trackData.getFilePath());
      System.out.println("\ttitle : " + trackData.getDataTitle());
      System.out.println("\tdata num  : " + datanum);

      // ログの開始、終了時刻
      System.out.println("\tStart time : " + trackData.getRecordDate().get(0));
      System.out.println("\tFinish time : " + trackData.getRecordDate().get(datanum - 1));

      System.out.println("\n");
    }

  }

  /**
   * @param dataSet
   */
  protected void showSummery(DataEmbedding dataSet) {

    System.out.println("\nsuccess: ");
    for (File file : dataSet.getCompleteFiles()) {
      System.out.println("\t" + file.getAbsolutePath());
    }

    System.out.println("\nskip: ");
    for (File file : dataSet.getOutOfRangeFiles()) {
      System.out.println("\t" + file.getAbsolutePath());
    }

    System.out.println("\nerror: ");
    for (File file : dataSet.getErrorOccuredFiles()) {
      System.out.println("\t" + file.getAbsolutePath());
    }

  }

}
