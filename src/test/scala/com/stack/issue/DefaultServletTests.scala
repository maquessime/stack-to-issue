package com.stack.issue

import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatest.FunSuiteLike
import com.github.simplyscala.MongoEmbedDatabase
import org.scalatest.BeforeAndAfter
import com.github.simplyscala.MongodProps
import com.stack.issue.repository.DefaultStackRepository
import com.mongodb.casbah.commons.MongoDBObject

final class DefaultServletTests extends ScalatraSuite with FunSuiteLike with MongoEmbedDatabase with BeforeAndAfter {
  val testStack = System.nanoTime.toString
  val hash = new Nilsimsa() hexdigest testStack
  val issue = "bug"
  val issueBody = s"""{"issues":["$issue"]}"""
  val anotherIssue = "anotherBug"
  val specialIssue = "specialIssue"
  val twoIssuesBody = s"""{"issues":["$anotherIssue","$specialIssue"]}"""

  val addIssuePath = "stacks/" + hash + "/issues"

  addServlet(classOf[DefaultServlet], "/*")

  var mongoProps: MongodProps = null

  before {
    mongoProps = mongoStart(27017)
  }

  after { mongoStop(mongoProps) }

  test("complete flow") {
    assertNoIssueIsLinkedToTheStack
    addAnIssue(issueBody)
    assertThatIssueIsLinkedToTheStack
    addAnIssue(twoIssuesBody)
    assertIssuesAreLinkedToTheStack
    deleteThisStack
    assertNoIssueIsLinkedToTheStack
  }

  def deleteThisStack {
    delete("stacks/" + hash) {
      status should equal(200)
    }
  }

  def assertIssuesAreLinkedToTheStack {
    post("/", testStack) {
      status should equal(200)
      body should include(issue)
      body should include(anotherIssue)
      body should include(specialIssue)
    }
  }


  def assertNoIssueIsLinkedToTheStack {
    post("/", testStack) {
      status should equal(200)
      body should not include issue
      body should not include anotherIssue
      body should not include specialIssue
    }
  }

  def addAnIssue(body:String) {
    post(addIssuePath, body) {
      status should equal(200)
    }
  }

  def assertThatIssueIsLinkedToTheStack {
    post("/", testStack) {
      status should equal(200)
      header("Content-Type") should include("application/json")
      body should include(issue)
    }
  }
}