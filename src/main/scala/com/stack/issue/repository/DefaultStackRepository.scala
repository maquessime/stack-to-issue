package com.stack.issue.repository

import scala.collection.immutable.List
import com.stack.issue.Nilsimsa
import com.stack.issue.domain.StackIssues
import com.mongodb.casbah.Imports._
import com.stack.issue.domain.StackIssues
import com.stack.issue.domain.Stack

final object DefaultStackRepository extends StackRepository {

  lazy val stacks = MongoClient("localhost", 27017)("test")("stacks")

  def findIssues(stack: String): StackIssues = {

    val hash = new Stack(stack).clean toNilsimsaHash
    val issues = findIssuesByHash(hash)
    if (issues.isEmpty) {
      new StackIssues(hash, issuesByNeighboorStacks(hash))
    } else {
      new StackIssues(hash, issues)
    }
  }

  private def issuesByNeighboorStacks(hash: String) = {
    val stackIssues = stacks.find($where(where(hash)))
    stackIssues.toList.map(extractIssuesFromDBO).flatten
  }

  def findIssuesByHash(hash: String): List[String] = {

    val queryParameter = MongoDBObject("hash" -> hash)
    val stackIssues = stacks.findOne(queryParameter)
    if (stackIssues.isEmpty) {
      List()
    } else {
      stackIssues.get.as[List[String]]("issues")
    }

  }

  def create(stackIssues: StackIssues) = {
    val stackIssuesDBO = dboFromDO(stackIssues)
    stacks.insert(stackIssuesDBO)
  }

  private def dboFromDO(stackIssues: StackIssues) = {
    MongoDBObject("hash" -> stackIssues.hash, "issues" -> stackIssues.issues)
  }

  def addIssue(stackIssues: StackIssues) = {
    val id = stackId(stackIssues.hash)
    val push = $push("issues" -> stackIssues.issues.head)
    stacks.update(id, push)
  }

  private def stackId(hash: String) = { MongoDBObject("hash" -> hash) }

  private def extractIssuesFromDBO: DBObject => List[String] = {
    dbObject => dbObject.as[List[String]]("issues")
  }

  private def where(hash: String): String = {
    s"""function(){ var hash=this.hash; var i=0; var distance=0; var key="$hash"; var length=key.length; while(i<length) { if(distance>16) { return false; } if(key.charAt(i)!=hash.charAt(i)) { distance++; } i++; } return true; }"""
  }

  def delete(hash: String) = {
    stacks.remove(stackId(hash))
  }

}