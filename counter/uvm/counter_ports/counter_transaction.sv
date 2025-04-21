`ifndef COUNTER_TRANSACTION__SV
`define COUNTER_TRANSACTION__SV

class counter_transaction extends uvm_sequence_item;
  rand bit rst;
  rand bit [3:0]  count;

  constraint rst_prob {
    rst dist { 0 := 90, 1 := 10 };
  }

  `uvm_object_utils_begin(counter_transaction)
    `uvm_field_int(rst, UVM_ALL_ON)
    `uvm_field_int(count, UVM_ALL_ON)
  `uvm_object_utils_end

  function new(string name = "counter_transaction");
    super.new(name);
  endfunction
endclass

`endif
