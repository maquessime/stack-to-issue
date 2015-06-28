package com.stack.issue.repository

import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatest.FunSuiteLike
import com.stack.issue.domain.StackIssues
import com.stack.issue.domain.CleanStack
import com.stack.issue.domain.Nilsimsa
import org.scalatest.BeforeAndAfter

final class InMemoryStackRepositoryUnitTests extends ScalatraSuite with FunSuiteLike with BeforeAndAfter {

  val stack = "balballba"
  val hash = new CleanStack(stack).toNilsimsaHash
  val issue = "ISSUE"
  val anotherIssue = "ANOTHER_ISSUE"
  val issues = List(issue)
  val crappyIssue="CRAPPY"
  
  test("test that issues can be found with perfect hash match") {

    InMemoryStackRepository.create(new StackIssues(hash, issues))

    val stackIssues = InMemoryStackRepository findIssues stack

    stackIssues.issues should contain(issue)
  }

  test("test that issue can be found if it's similar to another") {
    givenStack("balballbabalballbabalballbabalballbabalballbabalballbabalballba")
    val similarStack = "xalballbabalballbabalballbabxlballbabalballbabalballbabalbalxba"

    val stackIssues = InMemoryStackRepository findIssues similarStack

    stackIssues.issues should contain(issue)
  }

  test("test that issue is not found if stacks are totally different") {
    givenStack("balballbabalballbabalballbabalballbabalballbabalballbabalballba")
    val totallyDifferentStack = "1897978jojj9--9090-"

    val stackIssues = InMemoryStackRepository findIssues totallyDifferentStack

    stackIssues.issues should not contain (issue)
  }
  
  test("test that issue can be added"){
    InMemoryStackRepository create new StackIssues(hash, issues)
    InMemoryStackRepository addIssue new StackIssues(hash, List(crappyIssue))
    
    val stackIssues = InMemoryStackRepository findIssues stack
    
    stackIssues.issues should contain (crappyIssue)
  }

  def givenStack(stack: String) = {
    val hash = new CleanStack(stack).toNilsimsaHash
    InMemoryStackRepository.create(new StackIssues(hash, issues))
  }

  after {
    InMemoryStackRepository.delete(hash)
  }
}