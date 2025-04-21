from UT_ALU import DUTALU
from env import *
from toffee import *
from random import randint
import toffee_test
import pytest

# @toffee_test.testcase
# async def test_alu(alu_env: ALUEnv):
#     for sel in range(16):
#         for a in range(256):
#             for b in range(256):
#             # a = randint(0, 256)
#             # b = randint(0, 256)
#                 await alu_env.alu_agent.calc(a, b, sel)

@pytest.mark.parametrize("sel", range(16))
@pytest.mark.asyncio
@toffee_test.testcase
async def test_alu_grouped_by_sel(alu_env, sel):
    for a in range(256):
        for b in range(256):
            await alu_env.alu_agent.calc(a, b, sel)

@toffee_test.fixture
async def alu_env(toffee_request: toffee_test.ToffeeRequest):
    dut = toffee_request.create_dut(DUTALU)
    start_clock(dut)
    return ALUEnv(ALUBundle().bind(dut))
