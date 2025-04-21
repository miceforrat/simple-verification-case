import pyuvm
import random
from pyuvm import *
from counter_env import CounterEnv, CounterSeqItem
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
            seq_item = CounterSeqItem()
            seq_item.randomize()
            await self.start_item(seq_item)
            await self.finish_item(seq_item)

async def init_dut(dut):
    dut.rst.value = 1
    await RisingEdge(dut.clk)
    dut.rst.value = 0
    await RisingEdge(dut.clk)

@pyuvm.test()
class CounterRandomTest(uvm_test):
    def build_phase(self):
        self.env = CounterEnv("env", self)

    def end_of_elaboration_phase(self):
        self.random_seq = RandomSeq.create("random_seq")

    async def run_phase(self):
        self.raise_objection()
        cocotb.start_soon(generate_clock(cocotb.top))
        await init_dut(cocotb.top)

        seqr = ConfigDB().get(None, "", "SEQR")
        await self.random_seq.start(seqr)
        self.drop_objection()
