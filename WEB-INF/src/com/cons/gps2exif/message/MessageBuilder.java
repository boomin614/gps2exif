package com.cons.gps2exif.message;


public class MessageBuilder {

  /**
   * アプリケーション本体関連
   */
  static public final String VERSION = "1.0";
  static public final String SOFTWARE_NAME = "gps2exif";

  /**
   * アプリケーション処理ログ
   */
  static public final String APP_STARTT = SOFTWARE_NAME + " is started.";
  static public final String APP_FINISH = SOFTWARE_NAME + " is finished.";
  static public final String APP_Unexpected_FINISH = "Unexpected error has occured.";


  /**
   * オプション関連
   */
  static public final String ARG_OPTION_JPG = "target jpg/jpeg file(s), or stored directory with them.";
  static public final String ARG_OPTION_GPS = "target gps data file(s) which KML format, or stored directory with them.";
  static public final String ARG_OPTION_OUTPUT = "output direcotry of photo file(s).";
  static public final String ARG_OPTION_ADJUST_PHOTO = "a time adjustment of jpg files: minute";
  static public final String ARG_OPTION_ADJUST_GPS = "a time adjustment of gps data: hour";
  static public final String ARG_OPTION_MAKE_DIR = "create output directory if there is not.";
  static public final String ARG_OPTION_PROGRESS = "show progress of processing.";
  static public final String ARG_OPTION_KML_PROP = "show gps(kml/kmz/gpx) file properties.";
  static public final String ARG_OPTION_SUMMERY = "show summery of result.";
  static public final String ARG_OPTION_SHOW_KML_LOG = "show kml(kmz) raw data.";
  static public final String ARG_OPTION_HELP = "show usages.";
  static public final String ARG_INTERPORATION_0 = "the detamination method of the position: the first point after photography time";
  static public final String ARG_INTERPORATION_1 = "the detamination method of the position: the nearist point of photography time";
  static public final String ARG_INTERPORATION_2 = "the detamination method of the position: interporation by using two points";
  static public final String ARG_NOT_DUPLI_INTERPORATION = "can not select duplicate options of -i#: then -i0 is set";
  static public final String ARG_NO_UPDATE = "only analyzing gps log.";

  /**
   * エラー関連
   */
  static public final String ARG_DIR_ERROR = "there is no file or directory which you designate: ";
  static public final String MAKING_DIR_FAILED = "Making output directory is failed.";
  static public final String NO_DIR_EXISTED = "designated file or directory is not exist: ";
  static public final String NO_DATE_INFO = "gps tag is not updated if the photography date information cannnot get from jpg files.";
  static public final String GPS_EXT_INCORRECT_ERROR = "the extension of GPS log must be kmz or kml.";
  static public final String JPG_EXT_INCORRECT_ERROR = "the extension of JPG file must be jpg or jpeg.";
  static public final String XML_PARSE_EXCEPTION = "Illeagal fomat kmz/kml file(s) is given..";
  static public final String READ_OR_WRTITE_IMAGE_ERROR = "Unexpected error has occured during reading or writing image file: ";


}
