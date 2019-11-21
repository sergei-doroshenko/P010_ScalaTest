package samples.message

import java.awt.Desktop
import java.net.URI

object Messages {
  def main(args: Array[String]): Unit = {
    val toPr: Printable = new ConsoleMessage("hello")
    toPr.print()
    TheLinkToGoogle.click()
  }
}

class TextMessage(val txt: String)

trait Printable {
  def print()
}

class ConsoleMessage(txt: String) extends TextMessage(txt) with Printable {
  def this() {
    this("Default message")
  }

  override def print(): Unit = Console.print(txt)
}

trait Clickable {
  def click()
}

trait Browser {
  def open(href: String)
}

abstract class HyperLink(txt: String, href: String) extends TextMessage(txt) with Clickable {
  def browser: Browser
  override def click(): Unit = browser.open(href)
}

object DefaultBrowser extends Browser {
  def takeDesktop(): Option[Desktop] = {
    Option.when(Desktop.isDesktopSupported) (Desktop.getDesktop)
  }
  override def open(href: String): Unit = takeDesktop().foreach(_.browse(new URI(href)))
}

object TheLinkToGoogle extends HyperLink(txt = "Google", href = "www.google.com") {
  override def browser: Browser = DefaultBrowser
}