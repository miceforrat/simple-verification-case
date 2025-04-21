`ifndef ALU_TRANSACTION__SV
`define ALU_TRANSACTION__SV

class alu_transaction extends uvm_sequence_item;

  rand bit [7:0] a;
	rand bit [7:0] b;				//alu数据输入
	rand bit [3:0] sel;		//功能选择
	rand bit [7:0] out;		//数据输出


  `uvm_object_utils_begin(alu_transaction)
    `uvm_field_int(a, UVM_ALL_ON)
    `uvm_field_int(b, UVM_ALL_ON)
    `uvm_field_int(sel, UVM_ALL_ON)
    `uvm_field_int(out, UVM_ALL_ON)
  `uvm_object_utils_end

  function new(string name = "alu_transaction");
    super.new(name);
  endfunction
endclass

`endif
