package net.tobysullivan.eventstore

import scala.concurrent.Future

trait EventStore {
  def writeEvent(event: Event): Future[Long]
}