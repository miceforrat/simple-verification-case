import copy
from pyuvm import *
from counter_agent import CounterSeqItem, CounterAgent

class CounterScoreboard(uvm_scoreboard):
    class AnalysisExport(uvm_analysis_export):
        def __init__(self, name, parent):
            super().__init__(name, parent)
            self.logger.setLevel(logging.ERROR)
            self.ref = CounterRef()

        def write(self, seq_item):
            std_item = copy.deepcopy(seq_item)
            std_item = self.ref.update(std_item)

            if std_item != seq_item:
                self.logger.error(f"SCOREBOARD_MISMATCH: Expected: {std_item}, Got: {seq_item}")
            else:
                self.logger.info("SCOREBOARD_MATCH")

    def build_phase(self):
        super().build_phase()
        self.ap_analysis_export = CounterScoreboard.AnalysisExport("ap_analysis_export", self)

class CounterRef():
    expected = 0
    shadow = 0

    def update(self, item:CounterSeqItem):
        self.expected = self.shadow

        if (item.rst):
            # self.expected = 0
            self.shadow = 0
        else:
            self.shadow += 1
            if self.shadow >= 16:
                self.shadow = 0
        item.count = self.expected
        return item

class CounterEnv(uvm_env):
    def build_phase(self):
        self.agent = CounterAgent("agent", self)
        self.scoreboard = CounterScoreboard("scoreboard", self)

    def connect_phase(self):
        self.agent.ap.connect(self.scoreboard.ap_analysis_export)
