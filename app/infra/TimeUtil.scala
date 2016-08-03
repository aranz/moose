package infra

import java.sql.Timestamp

/**
  * Created by hiro on 2016/08/03.
  */
object TimeUtil
{
  def currentTimestamp : Timestamp =
  {
    val date = new java.util.Date()
    new Timestamp(date.getTime())
  }
}
