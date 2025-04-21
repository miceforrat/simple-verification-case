import cocotb
import random
from pyuvm import *
from cocotb.triggers import *

clock_period = 1000
half_period = clock_period // 2


class ALUSeqItem(uvm_sequence_item):
    def __init__(self, name="ALUSeqItem", a=0, b=0, sel=0, out=0):
        super().__init__(name)
        self.a = a
        self.b = b
        self.sel = sel
        self.out = out

    def randomize(self):
        self.a = random.randint(0, 255)
        self.b = random.randint(0, 255)
        self.sel = random.randint(0, 15)
    
    def __eq__(self, other):
        if (type(other) != ALUSeqItem):
            return False
        return self.a == other.a and self.b == other.b and self.sel == other.sel and self.out == other.out

    def __str__(self):
        return f"a={self.a}, b={self.b}, sel={self.sel}, out={self.out}"

class ALUDriver(uvm_driver):
    def build_phase(self):
        self.dut = cocotb.top
    
    async def set_sel_vals(self, seq_item: ALUSeqItem):
        self.dut.a.value = seq_item.a
        self.dut.b.value = seq_item.b
        self.dut.alu_sel.value = seq_item.sel
        self.dut.alu_out.value = seq_item.out
        await Timer(clock_period, units="ns")

    async def run_phase(self):
        while(True):
            seq_item  = await self.seq_item_port.get_next_item()
            # print("???")
            await self.set_sel_vals(seq_item)
            self.seq_item_port.item_done()

class ALUMonitor(uvm_monitor):
    def build_phase(self):
        self.dut = cocotb.top
        self.ap = uvm_analysis_port("ap", self)
    
    async def run_phase(self):
        while True:
            await Timer(clock_period, units="ns")
            seq_item = ALUSeqItem()
            seq_item.a = self.dut.a.value
            seq_item.b = self.dut.b.value
            seq_item.sel = self.dut.alu_sel.value
            seq_item.out = self.dut.alu_out.value
            self.ap.write(seq_item)

class ALUAgent(uvm_agent):
    def build_phase(self):
        self.seqr = uvm_sequencer("seqr", self)
        self.driver = ALUDriver("driver", self)
        self.monitor = ALUMonitor("monitor", self)
        ConfigDB().set(None, "*", "SEQR", self.seqr)

    def connect_phase(self):
        self.ap = self.monitor.ap
        self.driver.seq_item_port.connect(self.seqr.seq_item_export)
