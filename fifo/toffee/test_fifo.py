from UT_FIFO import DUTFIFO
from env import *
from toffee import *
from random import randint
import toffee_test

@toffee_test.testcase
async def test_fifo(fifo_env: FIFOEnv):
    item_nr = 0
    for _ in range(10000):
        if (randint(0, 1) and item_nr < 8) or item_nr == 0:
            await fifo_env.fifo_agent.write(randint(0, (1<<8) - 1))
            item_nr += 1
        else:
            await fifo_env.fifo_agent.read()
            item_nr -= 1

@toffee_test.fixture
async def fifo_env(toffee_request: toffee_test.ToffeeRequest):
    dut = toffee_request.create_dut(DUTFIFO, "clk")
    dut.rst_n.value = 1
    start_clock(dut)
    return FIFOEnv(FIFOBundle().bind(dut))
