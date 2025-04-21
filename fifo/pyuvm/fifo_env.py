import copy
from pyuvm import *
from fifo_agent import FIFOSeqItem, FIFOAgent, CMD_READ, CMD_WRITE

class FIFOScoreboard(uvm_scoreboard):
    class AnalysisExport(uvm_analysis_export):
        def __init__(self, name, parent):
            super().__init__(name, parent)
            self.logger.setLevel(logging.ERROR)
            self.ref = FIFORef()

        def write(self, seq_item:FIFOSeqItem):
            if (seq_item.cmd == CMD_WRITE):
                full, empty = self.ref.write(seq_item.wdata)
                if (full != seq_item.full or empty != seq_item.empty):
                    uvm_error("WRITE_MISMATCH: ", 
                              f"expected full: {full}; empty: {empty};" + 
                              f"actual full: {seq_item.full}; empty: {seq_item.empty}")
            elif (seq_item.cmd == CMD_READ):
                rdata, full, empty = self.ref.read()
                if (rdata != seq_item.rdata
                    and full !=seq_item.full
                    and empty != seq_item.empty):
                    uvm_error("READ_MISMATCH: ", 
                              f"expected rdata: {rdata}, full: {full}; empty: {empty};" + 
                              f"actual rdata: {seq_item.rdata}, full: {seq_item.full}; empty: {seq_item.empty}")
            
            # std_item = self.ref.update(std_item)

            # if std_item != seq_item:
            #     self.logger.error(f"SCOREBOARD_MISMATCH: Expected: {std_item}, Got: {seq_item}")
            # else:
            #     self.logger.info("SCOREBOARD_MATCH")

    def build_phase(self):
        super().build_phase()
        self.ap_analysis_export = FIFOScoreboard.AnalysisExport("ap_analysis_export", self)

class FIFORef():
    fifo = []
    LEN = 8

    def is_full(self):
        return len(self.fifo) >= 8

    def is_empty(self):
        return len(self.fifo) == 0

    def write(self, wdata):
        full = self.is_full()
        empty = self.is_empty()
        if (not full):
            self.fifo.append(wdata)
        return full, empty

    def read(self):
        if not self.is_empty():
            read_res = self.fifo.pop(0)
        else:
            read_res = 0
        return read_res, self.is_full(), self.is_empty()


class FIFOEnv(uvm_env):
    def build_phase(self):
        self.agent = FIFOAgent("agent", self)
        self.scoreboard = FIFOScoreboard("scoreboard", self)

    def connect_phase(self):
        self.agent.ap.connect(self.scoreboard.ap_analysis_export)
