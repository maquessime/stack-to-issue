package com.stack.issue.repository

import com.stack.issue.domain.StackIssues
import java.util.concurrent.ConcurrentHashMap
import com.stack.issue.domain.CleanStack

object InMemoryStackRepository extends StackRepository {
  
  val stacksPerHash = new ConcurrentHashMap[String,StackIssues]
  
  def addIssue(stackIssues: StackIssues): Unit = {
    stacksPerHash.putIfAbsent(stackIssues.hash, stackIssues)
  }

  def create(stackIssues: StackIssues): Unit = {
    ???
  }

  def delete(hash: String): Unit = {
    ???
  }

  def findIssues(stack: String): StackIssues = {
    val hash = new CleanStack(stack).toNilsimsaHash
    stacksPerHash.get(hash)
  }
}