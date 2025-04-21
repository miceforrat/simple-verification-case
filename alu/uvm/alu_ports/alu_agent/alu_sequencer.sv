`ifndef ALU_SEQUENCER__SV
`define ALU_SEQUENCER__SV

`include "alu_ports/alu_transaction.sv"

class alu_sequencer extends uvm_sequencer #(alu_transaction);
   `uvm_component_utils(alu_sequencer)

   function new(string name, uvm_component parent);
      super.new(name, parent);
   endfunction
endclass

`endif
