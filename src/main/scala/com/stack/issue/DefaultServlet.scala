package com.stack.issue

import org.scalatra._
import scalate.ScalateSupport
import com.stack.issue.repository.DefaultStackRepository
import com.stack.issue.domain.StackIssues
import spray.json._
import com.stack.issue.domain.StackIssuesJsonProtocol._

class DefaultServlet extends StackToIssueStack {

  post("/") {
    val stackIssues: StackIssues = DefaultStackRepository.findIssues(request.body)
    response.setContentType("application/json")
    response.getWriter() println stackIssues.toJson
  }

  put("/stacks/:hash/issues/:issue") {
    try{
    	DefaultStackRepository.create(stackIssues)
    } catch {
      case exception:com.mongodb.MongoException => InternalServerError("Unable to create this stack : Maybe it already exists? try to update with a POST if it's the case.");
      case exception:Throwable => InternalServerError("Internal server error : "+exception.getMessage);
    }
  }

  post("/stacks/:hash/issues/:issue") {
    DefaultStackRepository.addIssue(stackIssues)
  }

  delete("/stacks/:hash") {
    DefaultStackRepository.delete(hash)
  }

  private def stackIssues = {
    StackIssues(hash, issues)
  }

  private def issues = {
    List(params("issue"))
  }

  private def hash = {
    params("hash")
  }

}
