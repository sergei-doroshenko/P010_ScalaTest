package finalTask.service

// Definition of the a build job and its possible status values
trait Status

object Successful extends Status

object Failed extends Status

// Trait defining successful and failure responses
trait Response

final case class OK(message: String) extends Response

final case class KO(reason: String) extends Response
