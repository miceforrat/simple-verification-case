import com.ut.UT_Counter


class CounterWrapper {
  private val dut = new UT_Counter()

  dut.InitClock("clk")

  def tick(): Int = {
    dut.Step()
    dut.count.U64.intValue
  }

  def reset(): Unit = {
    dut.rst.Set(1)
    dut.Step()
    dut.rst.Set(0)
  }
}
