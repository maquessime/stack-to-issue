package com.stack.issue

import org.scalatra._
import scalate.ScalateSupport
import com.stack.issue.repository.MongoStackRepository
import com.stack.issue.domain.StackIssues
import spray.json._
import com.stack.issue.domain.StackIssuesJsonProtocol._

class DefaultServlet extends AbstractServlet {

  post("/") {
    val stackIssues: StackIssues = MongoStackRepository.findIssues(request.body)
    response.getWriter() println stackIssues.toJson
  }

  put("/stacks/:hash/issues/:issue") {
    try{
    	MongoStackRepository.create(stackIssues)
    } catch {
      case exception:com.mongodb.MongoException => InternalServerError("Unable to create this stack : Maybe it already exists? try to update with a POST if it's the case.");
      case exception:Throwable => InternalServerError("Internal server error : "+exception.getMessage);
    }
  }

  post("/stacks/:hash/issues/:issue") {
    MongoStackRepository.addIssue(stackIssues)
  }

  delete("/stacks/:hash") {
    MongoStackRepository.delete(hash)
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
