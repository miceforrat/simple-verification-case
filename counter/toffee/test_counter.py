from UT_Counter import DUTCounter
from env import *
from toffee import *
from random import randint
import toffee_test

@toffee_test.testcase
async def test_counter(counter_env: CounterEnv):
    await counter_env.counter_agent.reset()
    for _ in range(10000):
        if not randint(0, 10):
            await counter_env.counter_agent.reset()
        else:
            await counter_env.counter_agent.tick()

@toffee_test.fixture
async def counter_env(toffee_request: toffee_test.ToffeeRequest):
    dut = toffee_request.create_dut(DUTCounter, "clk")
    start_clock(dut)
    return CounterEnv(CounterBundle().bind(dut))
