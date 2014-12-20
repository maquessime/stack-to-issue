package com.stack.issue.domain

import com.stack.issue.Nilsimsa

final class Stack(stack:String) {
  
	private val removeDate = "[A-Z][a-z]{2}.*?[1-9]*[0-9].*?[0-9]{2}:[0-9]{2}:[0-9]{2}".r replaceAllIn(_:String,"")
	private val removeUUID = "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}".r replaceAllIn(_:String,"")
	private val cleanStack = removeDate andThen removeUUID
  
	def clean = {
	  val cleanedStack =  cleanStack(stack)
	  new Stack(cleanedStack)
	}
	
	
	def toNilsimsaHash = {
	  new Nilsimsa() hexdigest stack
	}
	
	override def toString = {
	  stack
	}
}