import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class CounterSpec extends AnyFlatSpec with Matchers{
  private val counter = new CounterWrapper()
  private val ref = new CounterRef()

  "CounterWrapper" should "keep tick and reset right 100k times" in {
    counter.reset()
    ref.reset()
    val rand = Random(System.currentTimeMillis())
    var choice = 0
    for (i <- 0 until 100000){
      choice = rand.nextInt(100);
      if (choice < 31){
        counter.tick() shouldEqual ref.tick()

      } else {
        counter.reset()
        ref.reset()
      }
    }
  }

  private class CounterRef {
    private var count = 0

    def reset(): Unit = {
      count = 0
    }

    def tick(): Int = {
      val res = count
      count += 1
      count &= 0xF
      res
    }
  }
}
