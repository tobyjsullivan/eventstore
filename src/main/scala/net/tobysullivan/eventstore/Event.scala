package net.tobysullivan.eventstore

trait Event {
  val eventType: String
  val eventData: Map[String, String]
}

