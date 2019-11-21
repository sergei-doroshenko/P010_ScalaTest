package samples.message

object Messages2 extends App {
  val hybrid = new ConsoleMessageAndLink(
    txt = "Click me",
    href = "www.click-tracking.com?trackingId=38473847"
  )
  hybrid.print()
  hybrid.click()
}

trait TextMessage2 { def txt: String }

trait ConsoleMessage2 extends TextMessage2 with Printable {
  override def print(): Unit = Console.print(txt)
}

trait HyperLink2 extends TextMessage2 with Clickable {
  def href: String
  def browser: Browser
  override def click() = browser.open(href)
}

class ConsoleMessageAndLink(override val txt: String, override val href: String) extends ConsoleMessage2 with HyperLink2 {
  override def browser: Browser = DefaultBrowser
}
