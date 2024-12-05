import com.ut.UT_FIFO

class FIFOWrapper {
  private val fifo = new UT_FIFO()

  fifo.InitClock("clk")
  fifo.rst_n.Set(1)

  def write(wdata: Int): FIFOWriteRet = {
    fifo.wdata.Set(wdata)
    fifo.wr_en.Set(1)
    fifo.Step()
    fifo.wr_en.Set(0)
    new FIFOWriteRet(fifo.empty.U.intValue, fifo.full.U.intValue)
  }

  def read(): FIFOReadRet = {
    fifo.rd_en.Set(1)
    fifo.Step()
    fifo.rd_en.Set(0)
    fifo.Step()
    new FIFOReadRet(fifo.rdata.U.intValue, fifo.empty.U.intValue, fifo.full.U.intValue)
  }
}