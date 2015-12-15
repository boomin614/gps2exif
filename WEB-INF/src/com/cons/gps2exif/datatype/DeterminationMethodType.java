package com.cons.gps2exif.datatype;

/**
 * ファイルタイプ
 * 
 * @author saito
 */
public enum DeterminationMethodType {
  // 超過時点
  None,
  // 最も近いGPS取得点
  NearBy,
  // 補間
  Interpolation,
}
