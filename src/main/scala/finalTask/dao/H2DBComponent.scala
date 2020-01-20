package finalTask.dao

class H2DBComponent extends DBComponent {
  override val driver = slick.jdbc.H2Profile
  override val db = slick.jdbc.JdbcBackend.Database.forConfig("h2mem1")
}

object H2DBComponent {
  def apply(): H2DBComponent = new H2DBComponent()
}