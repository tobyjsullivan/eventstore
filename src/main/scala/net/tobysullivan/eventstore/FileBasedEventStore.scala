package net.tobysullivan.eventstore

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.future

import net.tobysullivan.eventstore.util.JSONHelper

object FileBasedEventStore {
  object Implicits {
    implicit val defaultFileAppender: FileAppender = new FileAppender {
      def append(filename: String, line: String) {
        this.synchronized {
          val append: Boolean = (new File(filename)).exists()
          
          val out = new PrintWriter(new BufferedWriter(new FileWriter(filename, append)))
          out.write(line)
          out.close()
        }
      }
    }
  }
}

class FileBasedEventStore(implicit private val fileAppender: FileAppender) extends EventStore {
  private val LOG_FILE = "events"
    
    
  // Mutable state. Keep it restricted to this scope
  private var nextEventNum: Long = 0L;

  def writeEvent(event: Event): Future[Long] = {
    // Avoid passing mutable state out of current closure
    val eventNum = nextEventNum
    nextEventNum += 1

    future {
      val jsonString = JSONHelper.stringify(event, eventNum)

      appendToFile(jsonString)

      eventNum
    }

  }

  private def appendToFile(line: String) {
    require(line != null, "line cannot be null")

    // Strip out newlines for a single line of JSON
    val cleanedAndFinal = line.replaceAll("\n", "") + "\n"

    fileAppender.append(LOG_FILE, cleanedAndFinal)
  }
}