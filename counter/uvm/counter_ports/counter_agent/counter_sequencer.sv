`ifndef COUNTER_SEQUENCER__SV
`define COUNTER_SEQUENCER__SV

`include "counter_ports/counter_transaction.sv"

class counter_sequencer extends uvm_sequencer #(counter_transaction);
   `uvm_component_utils(counter_sequencer)

   function new(string name, uvm_component parent);
      super.new(name, parent);
   endfunction
endclass

`endif
