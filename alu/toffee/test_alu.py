from UT_ALU import DUTALU
from env import *
from toffee import *
from random import randint
import toffee_test

@toffee_test.testcase
async def test_alu(alu_env: ALUEnv):
    for _ in range(10000):
        a = randint(0, 255)
        b = randint(0, 255)
        sel = randint(0, 15)
        await alu_env.alu_agent.calc(a, b, sel)

@toffee_test.fixture
async def alu_env(toffee_request: toffee_test.ToffeeRequest):
    dut = toffee_request.create_dut(DUTALU)
    start_clock(dut)
    return ALUEnv(ALUBundle().bind(dut))
