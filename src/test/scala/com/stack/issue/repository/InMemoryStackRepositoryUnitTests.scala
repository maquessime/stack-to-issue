package com.stack.issue.repository

import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatest.FunSuiteLike
import com.stack.issue.domain.StackIssues
import com.stack.issue.domain.CleanStack

final class InMemoryStackRepositoryUnitTests extends ScalatraSuite with FunSuiteLike {

  val issue = "ISSUE"
  val anotherIssue = "ANOTHER_ISSUE"

  test("test that issues can be found with perfect hash match") {
    val stack = "balballba"
    val hash = new CleanStack(stack).toNilsimsaHash
    InMemoryStackRepository.addIssue(new StackIssues(hash, List(issue, anotherIssue)))

    val stackIssues = InMemoryStackRepository findIssues stack

    stackIssues.issues should contain(issue)
  }
}