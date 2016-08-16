package controllers

import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import scala.util.matching.Regex

/**
  * Created by hiro on 2016/08/06.
  */
trait CustomConstraints {
  val passwordCheckConstraint : Constraint[String] = Constraint("constraints.passwordcheck")({
    plainText =>
      val allNumbers = """¥d*""".r
      val allLetters = """[A-Za-z]*""".r
      val errors = plainText match {
        case allNumbers() => Seq(ValidationError("error.password_is_all_number"))
        case allLetters() => Seq(ValidationError("error.password_is_all_letter" +
          ""))
        case _ => Nil
      }
      if(errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })
}
