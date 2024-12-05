import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util
import java.util.{ArrayDeque, Queue}
import scala.util.Random

class FIFOSpec extends AnyFlatSpec with Matchers{
  private val fifo = new FIFOWrapper()
  private val ref = new FIFORef()
  private val FULL = 8
  private val EMPTY = 0

  "FIFO" should "pass random test" in {
    val rand = Random()
    var num = 0
    for (i <- 0 until 10000){
      var choice = rand.nextInt(10)
      var writes = (num == EMPTY) | (num < FULL && choice < 7)
      if (writes) {
        var win = rand.nextInt(256)
        var exp = fifo.write(win)
        var act = ref.write(win)
        exp.empty shouldEqual act.empty
        exp.full shouldEqual act.full
        num += 1
      } else {
        var exp = fifo.read()
        var act = ref.read()
        exp.empty shouldEqual act.empty
        exp.full shouldEqual act.full
        exp.empty shouldEqual act.empty
        num -= 1
      }
    }
  }

  private class FIFORef {
    final private val queue: util.Queue[Integer] = new util.ArrayDeque[Integer]

    def write(in: Int): FIFOWriteRet = {
      val ret: FIFOWriteRet = new FIFOWriteRet(if (queue.isEmpty) 1
      else 0, if (queue.size == FULL) 1
      else 0)
      if (queue.size < FULL) queue.offer(in)
      ret
    }

    def read(): FIFOReadRet = {
      assert(!queue.isEmpty)
      val res: Int = queue.poll
      new FIFOReadRet(res, if (queue.isEmpty) 1
      else 0, if (queue.size == FULL) 1
      else 0)
    }
  }
}
