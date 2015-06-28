package com.stack.issue.repository

import com.stack.issue.domain.StackIssues
import scala.collection._
import scala.collection.convert.decorateAsScala._
import java.util.concurrent.ConcurrentHashMap
import com.stack.issue.domain.CleanStack
import com.stack.issue.domain.StackIssues
import org.apache.commons.codec.binary.Hex
import com.stack.issue.domain.Nilsimsa

object InMemoryStackRepository extends StackRepository {

  val stacksPerHash = new ConcurrentHashMap[String, StackIssues].asScala

  def addIssue(stackIssues: StackIssues): Unit = {
    val hash = stackIssues.hash
    val existingStackIssues = stacksPerHash.putIfAbsent(hash, stackIssues)
    if (existingStackIssues.isDefined) {
      val mergedIssues = stackIssues.issues ++ existingStackIssues.get.issues
      val newStackIssues = new StackIssues(hash, mergedIssues)
      stacksPerHash.put(hash, newStackIssues)
    }
  }

  def create(stackIssues: StackIssues): Unit = {
    addIssue(stackIssues)
  }

  def delete(hash: String): Unit = {
    stacksPerHash.remove(hash)
  }

  def findIssues(stack: String): StackIssues = {
    val hash = new CleanStack(stack).toNilsimsaHash
    stacksPerHash.getOrElse(hash, issuesFromSimilarStacks(hash))
  }

  private def issuesFromSimilarStacks(hash: String): StackIssues = {
    val hashes = stacksPerHash.keys filter {
      hashCandidate =>
        {
          val nilsimsaScore = new Nilsimsa().compareHexHashes(hash, hashCandidate)
          //FIXME : should be a parameter
          nilsimsaScore >= 96
        }
    }
    val issues = hashes map { currentHash => stacksPerHash.get(currentHash).get.issues }
    new StackIssues(hash, issues.flatMap { x => x } toList)
  }

}