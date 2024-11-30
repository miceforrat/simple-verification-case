from toffee import *

class FIFOBundle(Bundle):
    wr_en, rd_en, wdata, rdata, empty, full = Signals(6)

class FIFOAgent(Agent):
    @driver_method()
    async def write(self, wdata):
        self.bundle.wr_en.value = 1
        self.bundle.wdata.value = wdata
        await self.bundle.step()
        self.bundle.wr_en.value = 0
        return self.bundle.empty.value, self.bundle.full.value

    @driver_method()
    async def read(self):
        self.bundle.rd_en.value = 1
        await self.bundle.step()
        self.bundle.rd_en.value = 0
        await self.bundle.step()
        return self.bundle.rdata.value, self.bundle.empty.value, self.bundle.full.value

class FIFOModel(Model):
    def __init__(self):
        super().__init__()
        self.fifo = []

    @driver_hook(agent_name="fifo_agent")
    def write(self, wdata):
        ret = int(len(self.fifo) == 0), int(len(self.fifo) == 8)
        if len(self.fifo) < 8:
            self.fifo.append(wdata & 0xFF)
        return ret

    @driver_hook(agent_name="fifo_agent")
    def read(self):
        assert len(self.fifo) > 0, "FIFO is empty"
        return self.fifo.pop(0), int(len(self.fifo) == 0), int(len(self.fifo) == 8)

class FIFOEnv(Env):
    def __init__(self, fifo_bundle: FIFOBundle):
        super().__init__()
        self.fifo_agent = FIFOAgent(fifo_bundle)
        self.attach(FIFOModel())
