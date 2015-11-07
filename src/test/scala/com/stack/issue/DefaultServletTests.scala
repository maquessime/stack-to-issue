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
  val anotherIssue = "anotherBug"
  addServlet(classOf[DefaultServlet], "/*")

  var mongoProps: MongodProps = null

  before {
    mongoProps = mongoStart(27017) 
  } 

  after { mongoStop(mongoProps) }

  test("complete flow") {
    assertNoIssueIsLinkedToTheStack
    addAnIssue
    assertThatIssueIsLinkedToTheStack
    addAnotherIssue
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
    }
  }

  def addAnotherIssue {
    post("stacks/" + hash + "/issues/" + anotherIssue) {
      status should equal(200)
    }
  }

  def assertNoIssueIsLinkedToTheStack {
    post("/", testStack) {
      status should equal(200)
      body should not include issue
      body should not include anotherIssue
    }
  }

  def addAnIssue {
    put("stacks/" + hash + "/issues/" + issue) {
      status should equal(200)
    }
  }

  def addTheSameIssue {
    put("stacks/" + hash + "/issues/" + issue) {
      status should equal(500)
      body should include("Unable to create this stack")
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