package com.stack.issue.domain

import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatest.FunSuiteLike
import org.scalatra.test.scalatest.ScalatraSpec

final class StackTests extends ScalatraSuite with FunSuiteLike {

  test("dates are removed") {
    val stack = "balre Feb  29 07:59:59 balbla"

    cleanedStack(stack) should equal("balre  balbla")
  }

  private def cleanedStack(stackToClean: String) = {
    new Stack(stackToClean).clean.toString
  }

  test("uuid are removed") {
    val stack = "reookk 21942148-236e-4300-97b1-1478907b770c ooqq"

    cleanedStack(stack) should equal("reookk  ooqq")
  }
  
  test("clean up is done"){
    val stack = "bla Feb  29 07:59:59 and 21942148-236e-4300-97b1-1478907b770c but 19:19:55,054 no"
      
      cleanedStack(stack) should equal("bla  and  but  no")
  }
  
  test("time are removed"){
    val stack = "yeah 00:25:44,935 yeah"
      
      cleanedStack(stack) should equal("yeah  yeah")
  }

}