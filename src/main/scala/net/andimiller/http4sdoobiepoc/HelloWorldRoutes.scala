package net.andimiller.http4sdoobiepoc

import io.circe._
import org.http4s.HttpRoutes
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl.Http4sDsl
import cats._, cats.implicits._, cats.effect._

class HelloWorldRoutes[F[_]: Sync: Dao] extends Http4sDsl[F] {
  implicit def decode[F[_]: Sync, T: Decoder] = jsonOf[F, T]

  val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "cat" =>
        Ok(Dao[F].getCats.map(_.asJson))
      case GET -> Root / "cat" / name =>
        Ok(Dao[F].getCatByName(name).map(_.asJson))
      case req @ POST -> Root / "cat" =>
        Ok(req.as[Cat].flatMap(Dao[F].addCat).map(Json.fromInt))
    }
}
