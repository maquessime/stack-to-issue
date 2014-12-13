package com.stack.issue.repository

import com.stack.issue.domain.StackIssues

trait StackRepository {
  def findIssues(stack: String): StackIssues

  def create(stackIssues: StackIssues)

  def addIssue(stackIssues: StackIssues)

  def delete(hash: String)
}