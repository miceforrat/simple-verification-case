import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ALUSpec extends AnyFlatSpec with Matchers {
  val alu = ALUWrapper()
  val range = 0 until 256
  val limit = 0xFF
  "ALUWrapper" should "correctly process add calculation" in {
    for (a <- range; b <-range){
       alu.process(a, b, 0) shouldEqual (a + b) & limit
    }
  }

  "ALUWrapper" should "correctly process all calculation 1" in {
    val sel_range = 0 until 8
//    val ab = for {a <- range; b <- range} yield (a, b)
    for (a <- range; b <-range ; sel <- sel_range) {
        alu.process(a, b, sel) shouldEqual ALURef(a, b, sel)
    }
  }

  "ALUWrapper" should "correctly process all calculation 2" in {
    val sel_range = 8 until 16
    //    val ab = for {a <- range; b <- range} yield (a, b)
    for (a <- range; b <- range; sel <- sel_range) {
      alu.process(a, b, sel) shouldEqual ALURef(a, b, sel)
    }
  }

  def ALURef(a: Int, b:Int, sel:Int):Int = {
      val res = sel match {
        case 0 => (a+b) & limit
        case 1 => (a - b) & limit
        case 2 => (a * b) & limit
        case 3 => if (b != 0)  (a / b) else 0
        case 4 => (a << 1) & limit
        case 5 => (a >> 1) & limit
        case 6 => ((a << 1) | (a >> 7)) & limit
        case 7 => ((a >> 1) | (a << 7)) & limit
        case 8 => a & b
        case 9 => a | b
        case 10 => a ^ b
        case 11 => ~(a | b) & limit
        case 12 => ~(a & b) & limit
        case 13 => ~(a ^ b) & limit
        case 14 => if (a > b) 1 else 0
        case 15 => if (a == b) 1 else 0
        case _ =>  (a+b) & limit
      }

      res
  }


}





