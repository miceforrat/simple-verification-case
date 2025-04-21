from toffee import *

class ALUBundle(Bundle):
    a, b, alu_sel, alu_out = Signals(4)

class ALUAgent(Agent):
    @driver_method()
    async def calc(self, a, b, sel):
        self.bundle.a.value = a
        self.bundle.b.value = b
        self.bundle.alu_sel.value = sel
        await self.bundle.step()
        return self.bundle.alu_out.value

class ALUModel(Model):
    @driver_hook(agent_name="alu_agent")
    def calc(self, a, b, sel):
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

class ALUEnv(Env):
    def __init__(self, alu_bundle: ALUBundle):
        super().__init__()
        self.alu_agent = ALUAgent(alu_bundle)
        self.attach(ALUModel())
