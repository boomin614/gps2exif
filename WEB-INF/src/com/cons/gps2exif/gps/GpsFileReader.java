package com.cons.gps2exif.gps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.time.DateUtils;

import com.cons.gps2exif.datamodel.GPSTrack;
import com.cons.gps2exif.datamodel.MultiTrack;
import com.cons.gps2exif.datamodel.PlaceMark;
import com.cons.gps2exif.datamodel.Track;
import com.cons.gps2exif.datatype.FileType;

public class GpsFileReader {

  private InputStream is = null;

  private String filePath = "";

  // trackデータ：要素数0で初期化
  private ArrayList<PlaceMark> placeMarks = null;

  // 時差(時)：デフォルトでは日本標準時JST
  private Integer timeDiff = 9;


  // コンストラクタ
  public GpsFileReader() {
    this.placeMarks = new ArrayList<PlaceMark>(0);
  }


  public ArrayList<GPSTrack> gpsDecode(File file) throws IOException, XMLStreamException {

    // GPSデータから取得できたkmlから解析した全てのGPStrackデータ
    ArrayList<GPSTrack> gpsTracks = new ArrayList<GPSTrack>(0);

    // 拡張子で処理を分ける
    String suffix = FileUtility.getFileSuffix(file).toUpperCase();

    if (suffix.equals(FileType.KMZ.toString())) {

      gpsTracks = kmzDecode(file);

    } else if (suffix.equals(FileType.KML.toString()) || suffix.equals(FileType.XML.toString())) {

      gpsTracks = kmlDecode(file);

    } else if (suffix.equals(FileType.GPX.toString())) {

      // 未実装
      gpsTracks = gpxDecode(file);

    }

    return gpsTracks;

  }

  private ArrayList<GPSTrack> kmzDecode(File file) throws IOException, XMLStreamException {

    this.filePath = file.getAbsolutePath();

    ZipFile zf = new ZipFile(file);
    KmlReader kmzReader = new KmlReader();
    ArrayList<GPSTrack> gpsTracks = new ArrayList<GPSTrack>(0);

    for (Enumeration<? extends ZipArchiveEntry> e = zf.getEntries(); e.hasMoreElements();) {
      ZipArchiveEntry entry = e.nextElement();

      if (entry == null) {
        break;
      }

      if (entry.isDirectory()) {
        continue;
      }

      if (entry.getName().equals("doc.kml")) {
        is = zf.getInputStream(entry);

        placeMarks = kmzReader.readFromXML(is);
        gpsTracks.addAll(makeGpsTracks(placeMarks));

      }
    }

    zf.close();
    return gpsTracks;

  }

  private ArrayList<GPSTrack> kmlDecode(File file) throws IOException, XMLStreamException {

    KmlReader kmzReader = new KmlReader();
    ArrayList<GPSTrack> gpsTracks = new ArrayList<GPSTrack>(0);

    is = new FileInputStream(file);

    placeMarks = kmzReader.readFromXML(is);
    gpsTracks.addAll(makeGpsTracks(placeMarks));

    return gpsTracks;

  }

  private ArrayList<GPSTrack> gpxDecode(File file) throws IOException, XMLStreamException {

    GpxReader gpxReader = new GpxReader();
    ArrayList<GPSTrack> gpsTracks = new ArrayList<GPSTrack>(0);

    is = new FileInputStream(file);

    placeMarks = gpxReader.readFromXML(is);
    gpsTracks.addAll(makeGpsTracks(placeMarks));

    return gpsTracks;

  }

  private ArrayList<GPSTrack> makeGpsTracks(ArrayList<PlaceMark> placeMarks) {

    ArrayList<GPSTrack> gpsTracks = new ArrayList<GPSTrack>();

    for (PlaceMark placeMark : placeMarks) {
      GPSTrack gpsTrack = new GPSTrack();
      MultiTrack multitrack = placeMark.getMultiTrack();

      if (multitrack != null) {

        // System.out.println(multitrack.getAltitudeMode());
        // System.out.println(multitrack.getInterpolate());

        for (Track tmpTrack : multitrack.getTracks()) {

          if (tmpTrack != null && tmpTrack.getWhens().size() > 0) {
            gpsTrack = setGpsTrackFromTrack(tmpTrack);

            gpsTrack.setDataTitle(placeMark.getName() + " - " + placeMark.getTrackType());
            gpsTrack.setFilePath(filePath);

            gpsTracks.add(gpsTrack);

          } else {
            System.out.println(filePath + " : no GPS Track.");
          }

        }

      }

      gpsTrack = null;

    }

    return gpsTracks;
  }

  private GPSTrack setGpsTrackFromTrack(Track track) {

    GPSTrack gpsTrack = new GPSTrack();

    if (track.getWhens().size() == track.getCoords().size() && track.getSpeeds().size() == track.getBearings().size() && track.getWhens().size() == track.getSpeeds().size()
        && track.getSpeeds().size() == track.getAccuracies().size()) {

      int dataSize = track.getWhens().size();

      ArrayList<Date> recordDate = new ArrayList<>();
      ArrayList<Float> latitude = new ArrayList<>();
      ArrayList<Float> longitude = new ArrayList<>();
      ArrayList<Float> altitude = new ArrayList<>();
      ArrayList<Float> speed = new ArrayList<>();
      ArrayList<Float> bearing = new ArrayList<>();
      ArrayList<Float> accuracy = new ArrayList<>();


      for (int i = 0; i < dataSize; i++) {

        // 座標データをセット
        // 139.549413 35.427754 100.0999984741211
        Float coordinates[] = getCoord(track.getCoords().get(i), " ");

        longitude.add(coordinates[0]);
        latitude.add(coordinates[1]);
        altitude.add(coordinates[2]);

        // 時刻データをセット
        {
          // 時差処理のためCalendarを生成
          Calendar cal = Calendar.getInstance();
          cal.setTime(getRecordDate(track.getWhens().get(i)));
          cal.add(Calendar.HOUR, timeDiff);

          // 時差処理後の時刻をセット
          recordDate.add(cal.getTime());
        }

        // 速度をセット
        String value = "";
        value = track.getSpeeds().get(i);
        if (value.isEmpty()) {
          speed.add(0f);
        } else {
          speed.add(Float.parseFloat(value));
        }

        // 方位をセット
        value = track.getBearings().get(i);
        if (value.isEmpty()) {
          bearing.add(0f);
        } else {
          bearing.add(Float.parseFloat(value));
        }

        // 精度をセット
        value = track.getAccuracies().get(i);
        if (value.isEmpty()) {
          accuracy.add(-1f);
        } else {
          accuracy.add(Float.parseFloat(value));
        }

      }// for

      gpsTrack.setRecordDate(recordDate);
      gpsTrack.setLatitude(latitude);
      gpsTrack.setLongitude(longitude);
      gpsTrack.setAltitude(altitude);
      gpsTrack.setBearing(bearing);
      gpsTrack.setSpeed(speed);
      gpsTrack.setAccuracy(accuracy);

    } else {
      System.out.println("these sizes are not identical with each other.");
    }

    return gpsTrack;
  }

  /**
   * @return placeMark
   */
  public ArrayList<PlaceMark> getPlaceMarks() {
    return this.placeMarks;
  }


  public Float[] getCoord(String coord, String deli) {

    Float coordinates[] = {0f, 0f, 0f};
    String coordStr[] = coord.split(deli);

    coordinates[0] = Float.parseFloat(coordStr[0]);
    coordinates[1] = Float.parseFloat(coordStr[1]);
    coordinates[2] = Float.parseFloat(coordStr[2]);

    return coordinates;
  }


  public Date getRecordDate(String dateStr) {

    Date parsedDate = null;
    try {
      parsedDate = DateUtils.parseDate(dateStr, new String[] {"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"});
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return parsedDate;

  }

  /**
   * @return timeDiff
   */
  public Integer getTimeDiff() {
    return timeDiff;
  }

  /**
   * 時差(時)：デフォルトでは日本標準時JST
   * 
   * @param timeDiff セットする timeDiff
   */
  public void setTimeDiff(Integer timeDiff) {
    this.timeDiff = timeDiff;
  }

}
