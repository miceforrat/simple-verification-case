import cocotb
import random
from pyuvm import *
from cocotb.triggers import *

clock_period = 2000
half_period = clock_period // 2


class CounterSeqItem(uvm_sequence_item):
    def __init__(self, name="CounterSeqItem", rst=0, count=0):
        super().__init__(name)
        self.rst = rst
        self.count = count

    def randomize(self):
        if (random.randint(0, 9) < 9):
            self.rst = 0
        else:
            self.rst = 1
        self.rst = 0

    def __eq__(self, other):
        if (type(other) != CounterSeqItem):
            return False
        return self.rst == other.rst and self.count == other.count 

    def __str__(self):
        return f"rst={self.rst}, count={self.count}"

class CounterDriver(uvm_driver):
    def build_phase(self):
        self.dut = cocotb.top
    
    async def rst(self, seq_item: CounterSeqItem):
        self.dut.rst.value = seq_item.rst
        await Timer(clock_period, units="ns")

    async def run_phase(self):
        while(True):
            seq_item  = await self.seq_item_port.get_next_item()
            await self.rst(seq_item)
            self.seq_item_port.item_done()
            await Timer(clock_period, units="ns")

class CounterMonitor(uvm_monitor):
    def build_phase(self):
        self.dut = cocotb.top
        self.ap = uvm_analysis_port("ap", self)
    
    async def run_phase(self):
        while True:
            await Timer(clock_period, units="ns")
            seq_item = CounterSeqItem()
            seq_item.rst = self.dut.rst.value
            seq_item.count = self.dut.count.value
            self.ap.write(seq_item)

class CounterAgent(uvm_agent):
    def build_phase(self):
        self.seqr = uvm_sequencer("seqr", self)
        self.driver = CounterDriver("driver", self)
        self.monitor = CounterMonitor("monitor", self)
        ConfigDB().set(None, "*", "SEQR", self.seqr)

    def connect_phase(self):
        self.ap = self.monitor.ap
        self.driver.seq_item_port.connect(self.seqr.seq_item_export)
