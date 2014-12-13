package com.stack.issue

import java.util._
import org.apache.commons.codec.DecoderException
import org.apache.commons.codec.binary.Hex
import org.apache.commons.lang3.ArrayUtils
import Nilsimsa._
//remove if not needed
import scala.collection.JavaConversions._

object Nilsimsa {

  private val TRAN = Nilsimsa._getByteArray("02D69E6FF91D04ABD022161FD873A1AC" + "3B7062961E6E8F399D05144AA6BEAE0E" +
    "CFB99C9AC76813E12DA4EB518D646B50" +
    "23800341ECBB71CC7A867F98F2365EEE" +
    "8ECE4FB832B65F59DC1B314C7BF06301" +
    "6CBA07E81277493CDA46FE2F791C9B30" +
    "E300067E2E0F383321ADA554CAA729FC" +
    "5A47697DC595B5F40B90A3816D255535" +
    "F575740A26BF195C1AC6FF995D84AA66" +
    "3EAF78B32043C1ED24EAE63F18F3A042" +
    "57085360C3C0834082D709BD442A67A8" +
    "93E0C2569FD9DD8515B48A27289276DE" +
    "EFF8B2B7C93D45944B110D65D5348B91" +
    "0CFA87E97C5BB14DE5D4CB10A21789BC" +
    "DBB0E2978852F748D3612C3A2BD18CFB" +
    "F1CDE46AE7A9FDC437C8D2F6DF58724E")

  private val POPC = Nilsimsa._getByteArray("00010102010202030102020302030304" + "01020203020303040203030403040405" +
    "01020203020303040203030403040405" +
    "02030304030404050304040504050506" +
    "01020203020303040203030403040405" +
    "02030304030404050304040504050506" +
    "02030304030404050304040504050506" +
    "03040405040505060405050605060607" +
    "01020203020303040203030403040405" +
    "02030304030404050304040504050506" +
    "02030304030404050304040504050506" +
    "03040405040505060405050605060607" +
    "02030304030404050304040504050506" +
    "03040405040505060405050605060607" +
    "03040405040505060405050605060607" +
    "04050506050606070506060706070708")

  private def _getByteArray(hexString: String): Array[Byte] = {
    try {
      Hex.decodeHex(hexString.toCharArray())
    } catch {
      case e: DecoderException => {
        e.printStackTrace()
        null
      }
    }
  }
}

class Nilsimsa {

  private var count: Int = 0

  private var acc: Array[Int] = new Array[Int](256)

  private var lastch: Array[Int] = new Array[Int](4)

  reset()

  def update(s: String) {
    for (ch <- s.toCharArray()) {
      count += 1
      if (lastch(1) > -1) {
        acc(_tran3(ch, lastch(0), lastch(1), 0)) += 1
      }
      if (lastch(2) > -1) {
        acc(_tran3(ch, lastch(0), lastch(2), 1)) += 1
        acc(_tran3(ch, lastch(1), lastch(2), 2)) += 1
      }
      if (lastch(3) > -1) {
        acc(_tran3(ch, lastch(0), lastch(3), 3)) += 1
        acc(_tran3(ch, lastch(1), lastch(3), 4)) += 1
        acc(_tran3(ch, lastch(2), lastch(3), 5)) += 1
        acc(_tran3(lastch(3), lastch(0), ch, 6)) += 1
        acc(_tran3(lastch(3), lastch(2), ch, 7)) += 1
      }
      var i = 3
      while (i > 0) {
        lastch(i) = lastch(i - 1)
        i -= 1
      }
      lastch(0) = ch
    }
  }

  private def reset() {
    count = 0
    Arrays.fill(acc, 0.toByte)
    Arrays.fill(lastch, -1)
  }

  private def _tran3(a: Int,
    b: Int,
    c: Int,
    n: Int): Int = {
    val i = (c) ^ TRAN(n)
    (((TRAN((a + n) & 255) ^ TRAN(b & 0xff) * (n + n + 1)) +
      TRAN(i & 0xff)) &
      255)
  }

  def digest(): Array[Byte] = {
    var total = 0
    var threshold: Int = 0
    val digest = Array.ofDim[Byte](32)
    Arrays.fill(digest, 0.toByte)
    if (count == 3) {
      total = 1
    } else if (count == 4) {
      total = 4
    } else if (count > 4) {
      total = 8 * count - 28
    }
    threshold = total / 256
    for (i <- 0 until 256 if acc(i) > threshold) {
      digest(i >> 3) = ((1 << (i & 7)) + digest(i >> 3)).toByte
    }
    ArrayUtils.reverse(digest)
    digest
  }

  def hexdigest(): String = Hex.encodeHexString(digest())

  def digest(s: String): Array[Byte] = {
    reset()
    update(s)
    digest()
  }

  def hexdigest(s: String): String = Hex.encodeHexString(digest(s))

  def compare(cmp: Nilsimsa): Int = {
    var bits = 0
    var j: Int = 0
    val n1 = digest()
    val n2 = cmp.digest()
    for (i <- 0 until 32) {
      j = 255 & n1(i) ^ n2(i)
      bits += POPC(j)
    }
    128 - bits
  }
}
