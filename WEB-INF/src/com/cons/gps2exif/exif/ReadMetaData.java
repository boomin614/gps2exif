/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cons.gps2exif.exif;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.lang3.time.DateUtils;

public class ReadMetaData {

  private double lon_ref = 0d;
  private double lat_ref = 0d;
  private double lon = 0d;
  private double lat = 0d;


  /**
   * @return lon_ref
   */
  public double getLon_ref() {
    return lon_ref;
  }

  /**
   * @return lat_ref
   */
  public double getLat_ref() {
    return lat_ref;
  }

  /**
   * @return lon
   */
  public double getLon() {
    return lon;
  }

  /**
   * @return lat
   */
  public double getLat() {
    return lat;
  }

  public void showAllMetaData(final File file) throws ImageReadException, IOException {
    // get all metadata stored in EXIF format (ie. from JPEG or TIFF).
    final ImageMetadata metadata = Imaging.getMetadata(file);

    if (metadata instanceof JpegImageMetadata) {
      final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

      // Jpeg EXIF metadata is stored in a TIFF-based directory structure
      // and is identified with TIFF tags.
      // Here we look for the "x resolution" tag, but
      // we could just as easily search for any other tag.
      //
      // see the TiffConstants file for a list of TIFF tags.

      System.out.println("file: " + file.getPath());

      // print out various interesting EXIF tags.
      showTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_XRESOLUTION);
      showTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_DATE_TIME);
      showTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
      showTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
      showTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_ISO);
      showTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
      showTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_APERTURE_VALUE);
      showTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_BRIGHTNESS_VALUE);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_ALTITUDE_REF);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_ALTITUDE);

      System.out.println();

      // simple interface to GPS data
      final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
      if (null != exifMetadata) {
        final TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
        if (null != gpsInfo) {
          final String gpsDescription = gpsInfo.toString();
          final double longitude = gpsInfo.getLongitudeAsDegreesEast();
          final double latitude = gpsInfo.getLatitudeAsDegreesNorth();

          System.out.println("    " + "GPS Description: " + gpsDescription);
          System.out.println("    " + "GPS Longitude (Degrees East): " + longitude);
          System.out.println("    " + "GPS Latitude (Degrees North): " + latitude);
        }
      }

      // more specific example of how to manually access GPS values
      final TiffField gpsLatitudeRefField =
          jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
      final TiffField gpsLatitudeField =
          jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
      final TiffField gpsLongitudeRefField =
          jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
      final TiffField gpsLongitudeField =
          jpegMetadata.findEXIFValueWithExactMatch(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
      if (gpsLatitudeRefField != null && gpsLatitudeField != null && gpsLongitudeRefField != null
          && gpsLongitudeField != null) {
        // all of these values are strings.
        final String gpsLatitudeRef = (String) gpsLatitudeRefField.getValue();
        final RationalNumber gpsLatitude[] = (RationalNumber[]) (gpsLatitudeField.getValue());
        final String gpsLongitudeRef = (String) gpsLongitudeRefField.getValue();
        final RationalNumber gpsLongitude[] = (RationalNumber[]) gpsLongitudeField.getValue();

        final RationalNumber gpsLatitudeDegrees = gpsLatitude[0];
        final RationalNumber gpsLatitudeMinutes = gpsLatitude[1];
        final RationalNumber gpsLatitudeSeconds = gpsLatitude[2];

        final RationalNumber gpsLongitudeDegrees = gpsLongitude[0];
        final RationalNumber gpsLongitudeMinutes = gpsLongitude[1];
        final RationalNumber gpsLongitudeSeconds = gpsLongitude[2];

        // This will format the gps info like so:
        //
        // gpsLatitude: 8 degrees, 40 minutes, 42.2 seconds S
        // gpsLongitude: 115 degrees, 26 minutes, 21.8 seconds E

        System.out.println("    " + "GPS Latitude: " + gpsLatitudeDegrees.toDisplayString()
            + " degrees, " + gpsLatitudeMinutes.toDisplayString() + " minutes, "
            + gpsLatitudeSeconds.toDisplayString() + " seconds " + gpsLatitudeRef);
        System.out.println("    " + "GPS Longitude: " + gpsLongitudeDegrees.toDisplayString()
            + " degrees, " + gpsLongitudeMinutes.toDisplayString() + " minutes, "
            + gpsLongitudeSeconds.toDisplayString() + " seconds " + gpsLongitudeRef);

      }

      System.out.println();

      final List<ImageMetadataItem> items = jpegMetadata.getItems();
      for (int i = 0; i < items.size(); i++) {
        final ImageMetadataItem item = items.get(i);
        System.out.println("    " + "item: " + item);
      }

      System.out.println();
    }
  }

  public void showGpsData(final File file) throws ImageReadException, IOException {
    // get all metadata stored in EXIF format (ie. from JPEG or TIFF).
    final ImageMetadata metadata = Imaging.getMetadata(file);

    // System.out.println(metadata);

    if (metadata instanceof JpegImageMetadata) {
      final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

      // Jpeg EXIF metadata is stored in a TIFF-based directory structure
      // and is identified with TIFF tags.
      // Here we look for the "x resolution" tag, but
      // we could just as easily search for any other tag.
      //
      // see the TiffConstants file for a list of TIFF tags.

      System.out.println("file: " + file.getPath());

      // print out various interesting EXIF tags.
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
      showTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE);

      System.out.println();
    }
  }

  public boolean getGpsFromJpgAndSet(final File file) throws ImageReadException, IOException {
    // get all metadata stored in EXIF format (ie. from JPEG or TIFF).
    final ImageMetadata metadata = Imaging.getMetadata(file);

    // System.out.println(metadata);

    if (metadata instanceof JpegImageMetadata) {
      final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

      // Jpeg EXIF metadata is stored in a TIFF-based directory structure
      // and is identified with TIFF tags.
      // Here we look for the "x resolution" tag, but
      // we could just as easily search for any other tag.
      //
      // see the TiffConstants file for a list of TIFF tags.

      System.out.println("file: " + file.getPath());

      // print out various interesting EXIF tags.
      this.lat_ref =
          Double.parseDouble(getTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF));
      this.lat =
          Double.parseDouble(getTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LATITUDE));
      this.lon_ref =
          Double.parseDouble(getTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF));
      this.lon =
          Double.parseDouble(getTagValue(jpegMetadata, GpsTagConstants.GPS_TAG_GPS_LONGITUDE));

      return true;

    }

    return false;
  }

  public Date getPhotographyDate(final File file) throws ImageReadException, IOException {
    // get all metadata stored in EXIF format (ie. from JPEG or TIFF).
    final ImageMetadata metadata = Imaging.getMetadata(file);

    String rtnValue = "";

    if (metadata instanceof JpegImageMetadata) {
      final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

      // print out various interesting EXIF tags.
      rtnValue = getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);

      if (null == rtnValue || rtnValue.isEmpty()) {
        rtnValue = getTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
      }

      if (null == rtnValue || rtnValue.isEmpty()) {
        rtnValue = getTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_DATE_TIME);
      }

      if (null == rtnValue || rtnValue.isEmpty()) {
        return null;
      }

    }
    rtnValue = rtnValue.replace("'", "");

    return getDateFromTiffDateStr(rtnValue);
  }

  public Date getDateFromTiffDateStr(String dateStr) {

    Date parsedDate = null;
    try {
      parsedDate = DateUtils.parseDate(dateStr, new String[] {"yyyy:MM:dd HH:mm:ss"});
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return parsedDate;

  }


  private void showTagValue(final JpegImageMetadata jpegMetadata, final TagInfo tagInfo) {
    final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
    if (field == null) {
      System.out.println(tagInfo.name + ": " + "Not Found.");
    } else {
      System.out.println(tagInfo.name + ": " + field.getValueDescription());
    }
  }

  private String getTagValue(final JpegImageMetadata jpegMetadata, final TagInfo tagInfo) {
    final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
    if (field == null) {
      return null;
    } else {
      return field.getValueDescription();
    }
  }


  public static void printMetaData(final File file) throws ImageReadException, IOException,
      IllegalArgumentException, IllegalAccessException {

    // get all metadata stored in EXIF format (ie. from JPEG or TIFF).
    ImageMetadata metadata = Imaging.getMetadata(file);

    if (!(metadata instanceof JpegImageMetadata)) {
      throw new RuntimeException("Only support " + JpegImageMetadata.class.getSimpleName());
    }

    JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

    // JPEG EXIF metadata
    List<Field> exifTagfields = getTagInfoDefinedInClass(ExifTagConstants.class);
    System.out.println("=== " + ExifTagConstants.class.getSimpleName() + " ===");
    printTagInfos(exifTagfields, jpegMetadata);

    // Tiff metadata
    List<Field> tiffTagfields = getTagInfoDefinedInClass(TiffTagConstants.class);
    System.out.println("=== " + TiffTagConstants.class.getSimpleName() + " ===");
    printTagInfos(tiffTagfields, jpegMetadata);

    // Gps metadata
    List<Field> gpsTagfields = getTagInfoDefinedInClass(GpsTagConstants.class);
    System.out.println("=== " + GpsTagConstants.class.getSimpleName() + " ===");
    printTagInfos(gpsTagfields, jpegMetadata);

    // simple interface to EXIF jpegMetadata
    TiffImageMetadata exifMetadata = jpegMetadata.getExif();
    if (null != exifMetadata) {
      System.out.println("=== ExifMetadata ===");
      jpegMetadata.getExif().getAllFields().stream().forEach(System.out::println);
      System.out.println();
    }

    // All IImageMetadataItem
    List<ImageMetadataItem> items = jpegMetadata.getItems();
    printIImageMetadataItems(items);
  }

  private static void printTagInfos(List<Field> tagfields, JpegImageMetadata jpegMetadata)
      throws IllegalAccessException, IllegalArgumentException, ImageReadException {
    for (Field field : tagfields) {
      printTagValue(jpegMetadata, (TagInfo) field.get(null));
    }
    System.out.println();
  }

  private static void printIImageMetadataItems(List<ImageMetadataItem> items) {
    System.out.println("=== " + ImageMetadataItem.class.getSimpleName() + " in "
        + JpegImageMetadata.class.getSimpleName() + " ===");
    for (ImageMetadataItem item : items) {
      System.out.println(item);
    }
    System.out.println();
  }

  private static void printTagValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo)
      throws ImageReadException {
    try {
      // ---------------------------------
      // KEY PART: Extract EXIF by TagInfo
      // if you use findEXIFValueWithExactMatch,
      // the method throws NullPointerException when the provided tag does not present in the
      // metadata
      // ---------------------------------
      TiffField field = jpegMetadata.findEXIFValue(tagInfo);

      if (field != null) {
        // get actual stored value in metadata
        Object value = field.getValue();
        System.out.println(tagInfo.name + ": " + toString(value));
      }
    } catch (NullPointerException e) {
    }
  }

  private static List<Field> getTagInfoDefinedInClass(Class<?> clazz) {
    Field[] declaredFields = clazz.getDeclaredFields();
    return Arrays
        .stream(declaredFields)
        .filter(
            field -> TagInfo.class.isAssignableFrom(field.getType())
                && Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
  }

  public static String toString(Object value) {
    if (value instanceof int[]) {
      return Arrays.toString((int[]) value);
    } else if (value instanceof boolean[]) {
      return Arrays.toString((boolean[]) value);
    } else if (value instanceof byte[]) {
      return Arrays.toString((byte[]) value);
    } else if (value instanceof char[]) {
      return Arrays.toString((char[]) value);
    } else if (value instanceof long[]) {
      return Arrays.toString((long[]) value);
    } else if (value instanceof short[]) {
      return Arrays.toString((short[]) value);
    } else if (value instanceof double[]) {
      return Arrays.toString((double[]) value);
    } else if (value instanceof float[]) {
      return Arrays.toString((float[]) value);
    } else if (value.getClass().isArray()) {
      return Arrays.toString((Object[]) value);
    } else {
      return value.toString();
    }
  }

}
