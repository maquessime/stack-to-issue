package com.stack.issue.domain

import org.scalatest.FunSuiteLike
import org.scalatra.test.scalatest.ScalatraSuite

class NilsimsaTests extends ScalatraSuite with FunSuiteLike {

  // from : http://ixazon.dynip.com/~cmeclax/nilsimsa.html  
  test("compare hex hashes") {
    val spam="773e2df0a02a319ec34a0b71d54029111da90838cbc20ecd3d2d4e18c25a3025"
    val anotherSpam="47182cf0802a11dec24a3b75d5042d310ca90838c9d20ecc3d610e98560a3645"
    
    val nilsimsaScore=new Nilsimsa().compareHexHashes(spam, anotherSpam)
    
    nilsimsaScore should equal(92)
  }
}