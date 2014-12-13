package com.stack.issue.domain

import spray.json.DefaultJsonProtocol

case class StackIssues(hash: String, issues: List[String])

object StackIssuesJsonProtocol extends DefaultJsonProtocol {
  implicit val stackIssuesFormat = jsonFormat2(StackIssues)
}