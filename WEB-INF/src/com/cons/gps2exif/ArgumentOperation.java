package com.cons.gps2exif;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.cons.gps2exif.datatype.DeterminationMethodType;
import com.cons.gps2exif.message.MessageBuilder;

public class ArgumentOperation {

  // KMZが格納されているディレクトリあるいはファイルパス
  private String INPUT_KMZ_PATH = "";
  // 対象のjpgファイル
  private String INPUT_JPG_PATH = "";
  // 解析結果の出力パス
  private String OUTPUT_JPG_PATH = "./output";

  // JPGデータの補正時間（時間）。デフォルトは【0】
  private Integer ADJUST_PHOTO_TIME = 0;
  // GPSデータの補正時間（時間）。デフォルトはJSTの【9】
  private Integer ADJUST_GPS_TIME = 9;

  // 出力先ディレクトリの作成可否
  private boolean makeDirFlg = true;

  // 進捗の表示
  private boolean showProgressFlg = false;

  // 結果サマリの表示
  private boolean showResultSummeryFlg = false;

  // KMZ情報サマリの表示
  private boolean showKmzSummeryFlg = false;

  // 生GPSログの表示
  private boolean showRawGpsDataFlag = false;

  // 生GPSログの表示
  private boolean noUpdateJpgFileFlag = false;

  // 撮影時国からの位置決定方法
  private DeterminationMethodType decideMethod = DeterminationMethodType.None;

  // 撮影時国からの位置決定方法が重複定義されないようにするためのフラグ
  private boolean decideMethod_flag = false;


  /**
   * @return iNPUT_KMZ_PATH
   */
  public String getINPUT_KMZ_PATH() {
    return INPUT_KMZ_PATH;
  }


  /**
   * @return iNPUT_JPG_PATH
   */
  public String getINPUT_JPG_PATH() {
    return INPUT_JPG_PATH;
  }


  /**
   * @return oUTPUT_JPG_PATH
   */
  public String getOUTPUT_JPG_PATH() {
    return OUTPUT_JPG_PATH;
  }


  /**
   * @return aDJUST_PHOTO_TIME
   */
  public Integer getADJUST_PHOTO_TIME() {
    return ADJUST_PHOTO_TIME;
  }


  /**
   * @return aDJUST_GPS_TIME
   */
  public Integer getADJUST_GPS_TIME() {
    return ADJUST_GPS_TIME;
  }


  /**
   * @return makeDirFlg
   */
  public boolean isMakeDirFlg() {
    return makeDirFlg;
  }

  /**
   * @return showResultSummeryFlg
   */
  public boolean isShowResultSummeryFlg() {
    return showResultSummeryFlg;
  }


  /**
   * @return showProgressFlg
   */
  public boolean isShowProgressFlg() {
    return showProgressFlg;
  }


  /**
   * @return showKmzSummeryFlg
   */
  public boolean isShowKmzSummeryFlg() {
    return showKmzSummeryFlg;
  }

  /**
   * @return showRawGpsDataFlag
   */
  public boolean isShowRawGpsDataFlag() {
    return showRawGpsDataFlag;
  }

  /**
   * @return decideMethod
   */
  public DeterminationMethodType getDecideMethod() {
    return decideMethod;
  }

  /**
   * @return noUpdateJpgFileFlag
   */
  public boolean isNoUpdateJpgFileFlag() {
    return noUpdateJpgFileFlag;
  }

  // create the Options
  private Options options = new Options();

  public void parseArg(String[] args) throws ParseException {

    // TODO GPS解析期間の指定

    // 解析対象のjpgファイルあるいはその格納先ディレクトリ
    Option jpgfile = Option.builder("j") //
        .required() //
        .hasArg()//
        .argName("jpg file or dir") //
        .desc(MessageBuilder.ARG_OPTION_JPG) //
        .longOpt("jpg-file") //
        .numberOfArgs(1) //
        .valueSeparator('=') //
        .build();
    options.addOption(jpgfile);

    // 解析対象のjpgファイルあるいはその格納先ディレクトリ
    Option gpsfile = Option.builder("g") //
        .required() //
        .hasArg()//
        .argName("gps file or dir") //
        .desc(MessageBuilder.ARG_OPTION_GPS) //
        .longOpt("gps-file") //
        .numberOfArgs(1) //
        .valueSeparator('=') //
        .build();
    options.addOption(gpsfile);

    // 処理対象の出力先ディレクトリ
    Option outputDir = Option.builder("o") //
        .hasArg()//
        .argName("outputDir") //
        .desc(MessageBuilder.ARG_OPTION_OUTPUT) //
        .longOpt("output") //
        .numberOfArgs(1) //
        .valueSeparator('=') //
        .build();
    options.addOption(outputDir);

    // 処理対象画像の調整
    Option adjustPhotoTime = Option.builder("AJ") //
        .hasArg()//
        .argName("adjustPhotoTime") //
        .desc(MessageBuilder.ARG_OPTION_ADJUST_PHOTO) //
        .longOpt("adjust-photo-time") //
        .numberOfArgs(1) //
        .valueSeparator('=') //
        .build();
    options.addOption(adjustPhotoTime);

    // 処理対象の出力先ディレクトリ
    Option adjustGpsTime = Option.builder("AG") //
        .hasArg()//
        .argName("adjustGpsTime") //
        .desc(MessageBuilder.ARG_OPTION_ADJUST_GPS) //
        .longOpt("adjust-gps-time") //
        .numberOfArgs(1) //
        .valueSeparator('=') //
        .build();
    options.addOption(adjustGpsTime);

    // 出力先のディレクトリがない場合に作成するか否か
    options.addOption("m", "makedir", false, MessageBuilder.ARG_OPTION_MAKE_DIR);

    // 進捗表示の有無
    options.addOption("p", "progress", false, MessageBuilder.ARG_OPTION_PROGRESS);

    // 結果サマリ表示の有無
    options.addOption("s", "summery", false, MessageBuilder.ARG_OPTION_SUMMERY);

    // KML概要表示の有無
    options.addOption("k", "gps_prop", false, MessageBuilder.ARG_OPTION_KML_PROP);

    // GPS位置の決定方法：初めて超過したポイント
    options.addOption("i0", null, false, MessageBuilder.ARG_INTERPORATION_0);

    // GPS位置の決定方法：初めて超過したポイントあるいはその手前のポイントのうち、近い方
    options.addOption("i1", "nearby", false, MessageBuilder.ARG_INTERPORATION_1);

    // GPS位置の決定方法：初めて超過したポイントとその手前のポイントで、時間按分で補間
    options.addOption("i2", "interporation", false, MessageBuilder.ARG_INTERPORATION_2);

    // KML概要表示の有無
    options.addOption("l", "log", false, MessageBuilder.ARG_OPTION_SHOW_KML_LOG);

    // KML概要表示の有無
    options.addOption("no", "noupdate", false, MessageBuilder.ARG_NO_UPDATE);

    // ヘルプの表示
    options.addOption("h", "help", false, MessageBuilder.ARG_OPTION_HELP);

    // create the command line parser
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;

    // parse the command line arguments
    try {

      // 引数の解析
      cmd = parser.parse(options, args);

      // 引数のパラメタの設定
      setArgParam(cmd);

    } catch (ParseException exp) {
      // System.out.println("Unexpected exception:" + exp.getMessage());
      // 必須オプションが設定されていないので、HELPを出して終了。
      HelpFormatter help = new HelpFormatter();
      help.setWidth(200);

      // TODO help表示時の並び順定義
      // help.setOptionComparator(new Comparator<Option>() {
      // @Override
      // public int compare(Option o1, Option o2) {
      // Option opt1 = (Option) o1;
      // Option opt2 = (Option) o2;
      // return opt2.getKey().compareToIgnoreCase(opt1.getKey());
      // }
      // });

      help.printHelp(MessageBuilder.SOFTWARE_NAME, options, true);

      throw exp;
    }


  }

  private void setArgParam(CommandLine cmd) {

    /**
     * 必須オプション
     */

    if (cmd.hasOption("j") | cmd.hasOption("jpg-file")) {
      INPUT_JPG_PATH = cmd.getOptionValue("j");
    }

    if (cmd.hasOption("g") | cmd.hasOption("gps-file")) {
      INPUT_KMZ_PATH = cmd.getOptionValue("g");
    }

    if (cmd.hasOption("o") | cmd.hasOption("output")) {
      OUTPUT_JPG_PATH = cmd.getOptionValue("o");
    }


    /**
     * パラメタ付　任意オプション　
     */

    if (cmd.hasOption("AJ") | cmd.hasOption("adjust-photo-time")) {
      ADJUST_PHOTO_TIME = Integer.parseInt(cmd.getOptionValue("AJ"));
    }

    if (cmd.hasOption("AG") | cmd.hasOption("adjust-gps-time")) {
      ADJUST_GPS_TIME = Integer.parseInt(cmd.getOptionValue("AG"));
    }


    /**
     * パラメタ無し　任意オプション　
     */

    if (cmd.hasOption("m") | cmd.hasOption("makedir")) {
      makeDirFlg = true;
    }

    if (cmd.hasOption("p") | cmd.hasOption("progress")) {
      showProgressFlg = true;
    }

    if (cmd.hasOption("s") | cmd.hasOption("summery")) {
      showResultSummeryFlg = true;
    }

    if (cmd.hasOption("k") | cmd.hasOption("kml_prop")) {
      showKmzSummeryFlg = true;
    }

    if (!decideMethod_flag) {
      if (cmd.hasOption("i0")) {
        decideMethod = DeterminationMethodType.None;
      } else if (cmd.hasOption("i1")) {
        decideMethod = DeterminationMethodType.NearBy;
      } else if (cmd.hasOption("i2")) {
        decideMethod = DeterminationMethodType.Interpolation;
      }
      // 設定されたフラグを立てる
      decideMethod_flag = true;
    } else {
      // -i# オプションは重複指定できない
      System.out.println(MessageBuilder.ARG_NOT_DUPLI_INTERPORATION);
      decideMethod = DeterminationMethodType.None;
    }

    if (cmd.hasOption("l") | cmd.hasOption("log")) {
      showRawGpsDataFlag = true;
    }

    if (cmd.hasOption("no") | cmd.hasOption("noupdate")) {
      noUpdateJpgFileFlag = true;
    }

    if (cmd.hasOption("h") | cmd.hasOption("help")) {
      HelpFormatter help = new HelpFormatter();
      help.printHelp(MessageBuilder.SOFTWARE_NAME, options, true);
    }

  }
}
