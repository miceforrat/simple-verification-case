import pyuvm
import random
from pyuvm import *
from fifo_env import FIFOEnv, FIFOSeqItem
from cocotb.triggers import *

async def generate_clock(dut):
    while True:
        dut.clk.value = 0
        await Timer(1000, units="ns")
        dut.clk.value = 1
        await Timer(1000, units="ns")

class RandomSeq(uvm_sequence):
    async def body(self):
        for _ in range(10000):
            seq_item = FIFOSeqItem()
            seq_item.randomize()
            seq_item.rst = 1
            await self.start_item(seq_item)
            await self.finish_item(seq_item)

async def init_dut(dut):
    dut.rst_n.value = 0
    await RisingEdge(dut.clk)
    dut.rst_n.value = 1
    await RisingEdge(dut.clk)

@pyuvm.test()
class FIFORandomTest(uvm_test):
    def build_phase(self):
        self.env = FIFOEnv("env", self)

    def end_of_elaboration_phase(self):
        self.random_seq = RandomSeq.create("random_seq")

    async def run_phase(self):
        self.raise_objection()
        cocotb.start_soon(generate_clock(cocotb.top))
        await init_dut(cocotb.top)

        seqr = ConfigDB().get(None, "", "SEQR")
        await self.random_seq.start(seqr)
        self.drop_objection()
