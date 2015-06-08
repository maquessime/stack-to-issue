package com.stack.issue.domain


final class CleanStack(dirtyStack:String) {
  
  
	private val removeDateTime = "[A-Z][a-z]{2}.*?[1-9]*[0-9].*?[0-9]{2}:[0-9]{2}:[0-9]{2}".r replaceAllIn(_:String,"")
	private val removeUUID = "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}".r replaceAllIn(_:String,"")
	private val removeTime = "[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}".r replaceAllIn(_:String,"")
  private val removeEndOfLine = "\n".r replaceAllIn(_:String,"")
	private val cleanStack = removeDateTime andThen removeUUID andThen removeEndOfLine andThen removeTime
  
  val stack = cleanStack(dirtyStack)
  
	def toNilsimsaHash = {
	  new Nilsimsa() hexdigest dirtyStack
	}
	
	override def toString = {
	  stack
	}
}

