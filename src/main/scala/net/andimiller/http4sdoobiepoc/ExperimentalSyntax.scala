package net.andimiller.http4sdoobiepoc

import cats._, cats.implicits._, cats.data._
import org.http4s._

object ExperimentalSyntax {
  implicit class ExperimentalKleisliSyntax[F[_]: Functor](service: Kleisli[F, Request[F], Response[F]]) {
    def imapK[G[_]: Monad](fg: F ~> G, gf: G ~> F): Kleisli[G, Request[G], Response[G]] =
      Kleisli { r: Request[G] => r.mapK(gf).pure[G] }.andThen(service.mapK(fg).mapF(req => req.map(_.mapK(fg))))
  }
}
