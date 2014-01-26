package net.tobysullivan.eventstore

trait FileAppender {
  def append(filename: String, line: String)
}