package net.andimiller.http4sdoobiepoc

import cats.effect._
import cats._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import doobie._
import doobie.implicits._
import ExperimentalSyntax._

object HelloWorldServer extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    xa[IO].use { fg =>
      fg(Dao[ConnectionIO].populate) *> BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(
          Router(
            "/" -> new HelloWorldRoutes[ConnectionIO].routes
          ).orNotFound.imapK(fg, gf)
        ).serve.compile.drain.as(ExitCode.Success)
    }

  val gf = new ~>[IO, ConnectionIO] {
    override def apply[A](fa: IO[A]): doobie.ConnectionIO[A] = LiftIO[ConnectionIO].liftIO(fa)
  }

  def xa[F[_]: ContextShift: Async](): Resource[F, ConnectionIO ~> F] = Resource.liftF(Sync[F].delay {
    val xa = Transactor.fromDriverManager[F]("org.sqlite.JDBC", "jdbc:sqlite:cats.db")
    new ~>[ConnectionIO, F] {
      override def apply[A](fa: doobie.ConnectionIO[A]): F[A] = fa.transact(xa)
    }
  })

}
