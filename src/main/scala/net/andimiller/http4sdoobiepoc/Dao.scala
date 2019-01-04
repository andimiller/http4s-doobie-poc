package net.andimiller.http4sdoobiepoc

import doobie._
import doobie.implicits._
import fs2.Stream

trait Dao[F[_]] {
  def populate: F[Int]
  def addCat(cat: Cat): F[Int]
  def getCatByName(name: String): F[Option[Cat]]
  def getCats(): Stream[F, Cat]
}

object Dao {
  def apply[F[_] : Dao]: Dao[F] = implicitly[Dao[F]]

  implicit val sqliteDao = new Dao[ConnectionIO] {
    def populate = sql"create table if not exists cats(name text, age int)".update.run
    def addCat(cat: Cat) = sql"insert into cats values(${cat.name}, ${cat.age})".update.run
    def getCatByName(name: String) = sql"select * from cats where name = $name".query[Cat].option
    def getCats() = sql"select * from cats".query[Cat].stream
  }
}
