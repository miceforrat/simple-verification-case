import com.ut.UT_ALU

import java.math.BigInteger;

class ALUWrapper {
    private val alu = new UT_ALU()

    def process(a: Int, b: Int, sel: Int): Int = {
      alu.a.Set(a)
      alu.b.Set(b)
      alu.alu_sel.Set(sel)
      alu.Step()
      alu.alu_out.Get().intValue()
    }


}
