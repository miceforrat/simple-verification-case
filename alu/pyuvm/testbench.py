import pyuvm
import random
from pyuvm import *
from alu_env import ALUEnv, ALUSeqItem
from cocotb.triggers import *

class RandomSeq(uvm_sequence):
    async def body(self):
        for sel in range(16):
            for a in range(256):
                for b in range(256):
                    seq_item = ALUSeqItem()
                    # seq_item.randomize()
                    seq_item.sel = sel
                    seq_item.a = a
                    seq_item.b = b
                    await self.start_item(seq_item)
                    await self.finish_item(seq_item)
            # for _ in range(15000):
            #     seq_item = ALUSeqItem()
            #     seq_item.randomize()
            #     seq_item.sel = sel
            #     await self.start_item(seq_item)
            #     await self.finish_item(seq_item)

@pyuvm.test()
class ALURandomTest(uvm_test):
    def build_phase(self):
        self.env = ALUEnv("env", self)

    def end_of_elaboration_phase(self):
        self.random_seq = RandomSeq.create("random_seq")

    async def run_phase(self):
        self.raise_objection()
        seqr = ConfigDB().get(None, "", "SEQR")
        await self.random_seq.start(seqr)
        self.drop_objection()
