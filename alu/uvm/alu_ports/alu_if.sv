`ifndef ALU_IF
`define ALU_IF

interface alu_if(input clock);
	logic [7:0] a;
	logic [7:0] b;
	logic [3:0] sel;	
	logic [7:0] out;		

endinterface //alu_if

`endif