package com.stack.issue

import org.scalatra._
import scalate.ScalateSupport
import com.stack.issue.repository.DefaultStackRepository
import com.stack.issue.domain.StackIssues
import spray.json._
import com.stack.issue.domain.StackIssuesJsonProtocol._
import com.stack.issue.domain.StackIssues

class DefaultServlet extends StackToIssueStack {

  post("/") {
    val stackIssues: StackIssues = DefaultStackRepository.findIssues(request.body)
    response.setContentType("application/json")
    response.getWriter() println stackIssues.toJson
  }

  post("/stacks/:hash/issues") {

    if (DefaultStackRepository.findIssuesByHash(hash).isEmpty) {
      DefaultStackRepository.create(stackIssues)
    } else {
      DefaultStackRepository.addIssue(stackIssues)
    }

  }

  delete("/stacks/:hash") {
    DefaultStackRepository.delete(hash)
  }

  private def stackIssues = {
    StackIssues(hash, issues)
  }

  private def issues = {
    request.body.parseJson.asJsObject.getFields("issues").toList map (x => x.toString)
  }

  private def hash = {
    params("hash")
  }

}
  