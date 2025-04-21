import copy
from pyuvm import *
from alu_agent import ALUSeqItem, ALUAgent

class ALUScoreboard(uvm_scoreboard):
    class AnalysisExport(uvm_analysis_export):
        def __init__(self, name, parent):
            super().__init__(name, parent)
            self.logger.setLevel(logging.ERROR)
            self.ref = ALURef()

        def write(self, seq_item):
            std_item = copy.deepcopy(seq_item)
            std_item = self.ref.calc(std_item)
            if std_item != seq_item:
                self.logger.error(f"SCOREBOARD_MISMATCH: Expected: {std_item}, Got: {seq_item}")
            else:
                self.logger.info("SCOREBOARD_MATCH")

    def build_phase(self):
        super().build_phase()
        self.ap_analysis_export = ALUScoreboard.AnalysisExport("ap_analysis_export", self)

class ALURef():
    
    def calc(self, item:ALUSeqItem):
        a = int(item.a)
        b = int(item.b)
        sel = item.sel
        if sel == 0b0000:  # Addition
            result = (a + b) & 0xFF
        elif sel == 0b0001:  # Subtraction
            result = (a - b) & 0xFF
        elif sel == 0b0010:  # Multiplication
            result = (a * b) & 0xFF
        elif sel == 0b0011:  # Division
            result = a // b if b != 0 else 0
        elif sel == 0b0100:  # Logical left shift
            result = (a << 1) & 0xFF
        elif sel == 0b0101:  # Logical right shift
            result = (a >> 1) & 0xFF
        elif sel == 0b0110:  # Rotate left
            result = ((a << 1) | (a >> 7)) & 0xFF
        elif sel == 0b0111:  # Rotate right
            result = ((a >> 1) | (a << 7)) & 0xFF
        elif sel == 0b1000:  # AND
            result = a & b
        elif sel == 0b1001:  # OR
            result = a | b
        elif sel == 0b1010:  # XOR
            result = a ^ b
        elif sel == 0b1011:  # NOR
            result = ~(a | b) & 0xFF
        elif sel == 0b1100:  # NAND
            result = ~(a & b) & 0xFF
        elif sel == 0b1101:  # XNOR
            result = ~(a ^ b) & 0xFF
        elif sel == 0b1110:  # Greater than comparison
            result = 1 if a > b else 0
        elif sel == 0b1111:  # Equality comparison
            result = 1 if a == b else 0
        else:  # Default case
            result = (a + b) & 0xFF

        item.out = result
        return item

class ALUEnv(uvm_env):
    def build_phase(self):
        self.agent = ALUAgent("agent", self)
        self.scoreboard = ALUScoreboard("scoreboard", self)

    def connect_phase(self):
        self.agent.ap.connect(self.scoreboard.ap_analysis_export)
