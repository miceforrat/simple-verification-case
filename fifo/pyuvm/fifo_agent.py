import cocotb
import random
from pyuvm import *
from cocotb.triggers import *

clock_period = 2000
half_period = clock_period // 2

CMD_WRITE = 1
CMD_READ = 0

class FIFOSeqItem(uvm_sequence_item):
    def __init__(self, name="FIFOSeqItem", cmd=0, wdata=0, rdata=0, empty=0, full=0, rst=0):
        super().__init__(name)
        self.rst = rst
        self.wdata = wdata
        self.rdata = rdata
        self.empty = empty
        self.full = full
        self.rst = rst

    def randomize(self):
        self.cmd = random.randint(CMD_READ, CMD_WRITE)
        self.wdata = random.randint(0, 255)
        

class FIFODriver(uvm_driver):
    def build_phase(self):
        self.dut = cocotb.top
    
    async def deal_with(self, seq_item: FIFOSeqItem):
        if (seq_item.rst):
            self.dut.rst_n.value = seq_item.rst
        else:
            if (seq_item.cmd == CMD_WRITE):
                await self.write(wdata=seq_item.wdata)

    async def write(self, wdata):
        self.dut.wr_en.value = 1
        self.dut.wdata.value = wdata
        await Timer(clock_period, units="ns")
        self.dut.wr_en.value = 0

    async def read(self):
        self.dut.rd_en.value = 1
        await Timer(clock_period, units="ns")
        self.dut.rd_en.value = 0

    async def run_phase(self):
        while(True):
            seq_item  = await self.seq_item_port.get_next_item()
            await self.deal_with(seq_item)
            self.seq_item_port.item_done()

class FIFOMonitor(uvm_monitor):
    def build_phase(self):
        self.dut = cocotb.top
        self.ap = uvm_analysis_port("ap", self)
    
    async def collect_infos(self, seq_item: FIFOSeqItem):
        await Timer(clock_period, units="ns")
        seq_item.empty = self.dut.empty.value
        seq_item.full = self.dut.full.value

    async def run_phase(self):
        while True:
            await Timer(clock_period, units="ns")
            seq_item = FIFOSeqItem()
            if (self.dut.wr_en.value == 1):
                seq_item.cmd = CMD_WRITE
                await self.collect_infos(seq_item)
                seq_item.wdata = self.dut.wdata.value
                self.ap.write(seq_item)
            elif (self.dut.rd_en.value == 1):
                seq_item.cmd = CMD_READ
                await self.collect_infos(seq_item)
                seq_item.rdata = self.dut.rdata.value
                self.ap.write(seq_item)

class FIFOAgent(uvm_agent):
    def build_phase(self):
        self.seqr = uvm_sequencer("seqr", self)
        self.driver = FIFODriver("driver", self)
        self.monitor = FIFOMonitor("monitor", self)
        ConfigDB().set(None, "*", "SEQR", self.seqr)

    def connect_phase(self):
        self.ap = self.monitor.ap
        self.driver.seq_item_port.connect(self.seqr.seq_item_export)
