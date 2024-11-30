from toffee import *

class CounterBundle(Bundle):
    rst, count = Signals(2)

class CounterAgent(Agent):
    @driver_method()
    async def tick(self):
        await self.bundle.step()
        return self.bundle.count.value

    @driver_method()
    async def reset(self):
        self.bundle.rst.value = 1
        await self.bundle.step()
        self.bundle.rst.value = 0

class CounterModel(Model):
    def __init__(self):
        super().__init__()
        self.count = 0

    @driver_hook(agent_name="counter_agent")
    def tick(self):
        ret, self.count = self.count, (self.count + 1) & 0xF
        return ret

    @driver_hook(agent_name="counter_agent")
    def reset(self):
        self.count = 0

class CounterEnv(Env):
    def __init__(self, counter_bundle: CounterBundle):
        super().__init__()
        self.counter_agent = CounterAgent(counter_bundle)
        self.attach(CounterModel())
