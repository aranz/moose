package controllers

import javax.inject._

import akka.actor.ActorSystem
import infra.TimeUtil
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}
import models.{User, UserRepository}
import play.api.i18n.{I18nSupport, MessagesApi}

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */
//object UserController extends UserController

@Singleton
class UserController @Inject()(val messagesApi: MessagesApi) extends Controller
  with I18nSupport with CustomConstraints {

  val now = TimeUtil.currentTimestamp
  val allNumbers = """¥d*"""
  val allLetters = """[A-Za-z]*"""
  val form : Form[User] = Form(
    mapping(
      "id" -> default(longNumber, 0L),
      "username" -> text.verifying(
        messagesApi("error.maxLength",30),
        {username : String => username.length() < 30}).verifying(
        messagesApi("error.required"),
        {username : String => !username.isEmpty()}),
      "password" -> text.verifying(
        messagesApi("error.required"),
        {password : String => !password.isEmpty()}).verifying(
        messagesApi("error.minLength",6),
        {password : String => password.length() >= 6}).verifying(
        messagesApi("error.password_is_all_number"),
        {password : String => !password.matches(allNumbers)}).verifying(
        messagesApi("error.password_is_all_letter"),
        {password : String => !password.matches(allLetters)}),
      "firstname" -> optional(text),
      "lastname" -> optional(text),
      "createdAt" -> ignored(now),
      "updatedAt" -> ignored(now)
    )(User.apply)(User.unapply))

  def addForm = Action { implicit request =>
    Ok(views.html.user.add_form(form))
  }

  def add = Action { implicit request =>
    form.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.user.add_form(errors))
      },
      user => {
        UserRepository.save(user)
        Redirect(routes.UserController.addForm)
      })
  }
}
