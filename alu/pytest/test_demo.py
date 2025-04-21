from UT_ALU import DUTALU
import pytest

def test_all():
    alu_wrapper = ALUWrapper()
    for sel in range(16):
        for a in range(256):
            for b in range(256):
                assert alu_wrapper.process(a, b, sel) == calc(a, b, sel)

class ALUWrapper():

    def __init__(self):
        self.alu = DUTALU()

    def process(self, a, b, sel):
        self.alu.a.value = a
        self.alu.b.value = b
        self.alu.alu_sel.value = sel
        self.alu.Step()
        return self.alu.alu_out.value


def calc(a, b, sel):
    # Ensure inputs are 8-bit
    a &= 0xFF
    b &= 0xFF
    result = 0

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

    return result    