echo off

set TOOL_DIR=%~dp0
cd /d %TOOL_DIR%

rem ** ------ 設定 ここから ------------------------------------------------------------**

set PATH=%PATH%

rem 画像ファイルを格納しているディレクトリ
set JPG_DIR="D:\旅行"

rem GPSデータ（KMZ/KML/gpxファイルが格納されているディレクトリ）を格納しているディレクトリ
set GPS_DIR="D:\My Tracks\旅行gpsログ"

rem 画像ファイルの出力先ディレクトリを指定
set OUT_DIR="./output"

rem ** ------ 設定 ここまで ------------------------------------------------------------**

java -jar gps2exif.jar -j %JPG_DIR% -g %GPS_DIR% -o %OUT_DIR% -p -s -k > gps2exif.log


rem usage: gps2exif [-AG <adjustGpsTime>] [-AJ <adjustPhotoTime>] -g <gps file or dir> [-h] -j <jpg file or dir> [-k] [-l] [-m] [-o <outputDir>] [-p] [-s]
rem -AG,--adjust-gps-time <adjustGpsTime>       a time adjustment of gps data: hour
rem -AJ,--adjust-photo-time <adjustPhotoTime>   a time adjustment of jpg files: minute
rem -g,--gps-file <gps file or dir>             target gps data file(s) which KML format, or stored directory with them.
rem -h,--help                                   show usages.
rem -i0                                         the detamination method of the position: the first point after photography time
rem -i1,--nearby                                the detamination method of the position: the nearist point of photography time
rem -i2,--interporation                         the detamination method of the position: interporation by using two points
rem -j,--jpg-file <jpg file or dir>             target jpg/jpeg file(s), or stored directory with them.
rem -k,--gps_prop                               show gps(kml/kmz/gpx) file properties.
rem -l,--log                                    show kml(kmz) raw data.
rem -m,--makedir                                create output directory if there is not.
rem -no,--noupdate                              only analyzing gps log.
rem -o,--output <outputDir>                     output direcotry of photo file(s).
rem -p,--progress                               show progress of processing.
rem -s,--summery                                show summery of result.

