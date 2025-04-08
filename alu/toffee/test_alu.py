from UT_ALU import DUTALU
from env import *
from toffee import *
from random import randint
import toffee_test
import toffee.funcov as fc
from toffee_test.reporter import set_func_coverage

@toffee_test.testcase
async def test_alu(alu_env: ALUEnv):
    # for _ in range(10000):
    #     a = randint(0, 255)
    #     b = randint(0, 255)
    #     sel = randint(0, 15)
    for sel in range(3):
        for a in range(256):
            for b in range(256):
                await alu_env.alu_agent.calc(a, b, sel)

gr = fc.CovGroup("ALU")

def init_cov(dut: DUTALU, group: fc.CovGroup):
    group.add_cover_point(dut, {"test add": lambda x: x.alu_sel.value == 0, "test sub": lambda x: x.alu_sel.value == 1, "test ?" : lambda x: x.alu_sel.value == 2}, "test cov grp")



@toffee_test.fixture
async def alu_env(toffee_request: toffee_test.ToffeeRequest):
    dut = toffee_request.create_dut(DUTALU)
    gr = fc.CovGroup("ALU")
    init_cov(dut, gr)
    toffee_request.add_cov_groups([gr])
    start_clock(dut)
    yield ALUEnv(ALUBundle().bind(dut))

    import asyncio
    cur_loop = asyncio.get_event_loop()
    for task in asyncio.all_tasks(cur_loop):
        if task.get_name() == "__clock_loop":
            task.cancel()
            try:
                await task
            except asyncio.CancelledError:
                break

                