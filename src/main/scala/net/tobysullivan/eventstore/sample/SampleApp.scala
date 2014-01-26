package net.tobysullivan.eventstore.sample

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Failure
import scala.util.Success

import net.tobysullivan.eventstore._
import net.tobysullivan.eventstore.FileBasedEventStore.Implicits._

object SampleApp extends App {
  case class UserCreatedEvent(username: String, firstName: String, lastName: String, age: Int) extends Event {
    val eventType = "UserCreated"
    
    lazy val eventData: Map[String, String] = Map(
        "username" -> username,
        "firstName" -> firstName,
        "lastName" -> lastName,
        "age" -> age.toString
      )
  }
  
  println("Generating user created events...")
  
  val events = Seq(
    UserCreatedEvent("tobyjsullivan", "Toby", "Sullivan", 27),
    UserCreatedEvent("billy", "William", "Shatner", 69),
    UserCreatedEvent("sampats", "Sam", "Pats", 32),
    UserCreatedEvent("jsmithe", "Justine", "Smithe", 21),
    UserCreatedEvent("the_lurke", "Robert", "Lurke", 17)
    
  )
  
  println("Writing events...")
  
  val eventStore = new FileBasedEventStore
  
  val futures = for {
    e <- events
  } yield eventStore.writeEvent(e)
  
  println("Waiting for futures...")
  
  for {
    f <- futures
  } f.onComplete {
    case Success(id) => println(s"Wrote event: $id")
    case Failure(t) => println("Exception: " + t.getMessage())
  }
  
  for {
    f <- futures
  } Await.ready(f, Duration.Inf)
  
  println("Futures complete.")
  
  Await
}