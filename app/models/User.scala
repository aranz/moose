package models

import java.sql.Timestamp

import infra.TimeUtil
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by hiro on 2016/08/03.
  */
case class User (id : Long,
                 username : String,
                 password : String,
                 firstname : Option[String] = None,
                 lastname : Option[String] = None,
                 createdAt : Timestamp,
                 updatedAt : Timestamp)

object User
{
  def create(username : String,
             password : String,
             firstname : Option[String] = None,
             lastname : Option[String] = None) : User =
  {
    require(Option(password).isDefined)
    val now = TimeUtil.currentTimestamp
    User(id = 0, username = username, password = password,
      firstname = firstname, lastname = lastname,
      createdAt = now, updatedAt = now)
  }
}

object UserRepository extends HasDatabaseConfig[JdbcProfile]
{
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  private val Users = TableQuery[UserTable]

  def find(id: Long) : Option[User] =
  {
    require(Option(id).isDefined)
    val action = Users.filter(_.id === id)
    Await.result(db.run(action.result.headOption), Duration.Inf)
  }

  def save(user: User) : Int =
  {
    require(Option(user).isDefined)
    val action = Users.insertOrUpdate(user)
    Await.result(db.run(action), Duration.Inf)
  }

  private class UserTable(tag: Tag) extends Table[User](tag, "users")
  {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def firstname = column[String]("firstname")
    def lastname = column[String]("lastname")
    def username = column[String]("username")
    def password = column[String]("password")
    def createdAt = column[Timestamp]("created_at")
    def updatedAt = column[Timestamp]("updated_at")
    def * = (id, firstname.?, lastname.?,
      username, password, createdAt, updatedAt) <> ((User.apply _).tupled, User.unapply _)
  }
}
