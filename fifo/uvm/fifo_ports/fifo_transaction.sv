`ifndef FIFO_TRANSACTION__SV
`define FIFO_TRANSACTION__SV

class fifo_transaction extends uvm_sequence_item;

  rand bit cmd;
  rand bit [7:0] wdata;
  bit [7:0] rdata;
  bit empty;
  bit full;
  rand bit rst;


  `uvm_object_utils_begin(fifo_transaction)
    `uvm_field_int(cmd, UVM_ALL_ON)
    `uvm_field_int(wdata, UVM_ALL_ON)
    `uvm_field_int(rdata, UVM_ALL_ON)
    `uvm_field_int(empty, UVM_ALL_ON)
    `uvm_field_int(full, UVM_ALL_ON)
    `uvm_field_int(rst, UVM_ALL_ON)
  `uvm_object_utils_end

  function new(string name = "fifo_transaction");
    super.new(name);
  endfunction
endclass

`endif
