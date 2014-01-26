package net.tobysullivan.eventstore.util

import net.tobysullivan.eventstore.Event
import scala.util.parsing.json._

object JSONHelper {
  def stringify(event: Event, eventNum: Long): String = {
    val outputMap = Map[String, Any](
      "EventNumber" -> eventNum,
      "EventType" -> event.eventType,
      "EventData" -> event.eventData)

    val jsonObj = new JSONObject(outputMap)
    
    jsonObj.toString(jsonFormatter)
  }

  private def jsonFormatter(o: Any): String = o match {
    case m: Map[String, _] => (new JSONObject(m)).toString(jsonFormatter)
    case _ => JSONFormat.defaultFormatter(o)
  }
}