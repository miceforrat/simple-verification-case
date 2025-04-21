`ifndef ALU_AGENT__SV
`define ALU_AGENT__SV

`include "alu_ports/alu_agent/alu_sequencer.sv"
`include "alu_ports/alu_agent/alu_driver.sv"
`include "alu_ports/alu_agent/alu_monitor.sv"

class alu_agent extends uvm_agent;
    alu_sequencer sqr;
    alu_driver drv;
    alu_monitor mon;

    uvm_analysis_port #(alu_transaction) ap;

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);

        if (is_active == UVM_ACTIVE) begin
            `uvm_info(get_type_name(), "ACTIVE", UVM_MEDIUM)
            sqr = alu_sequencer::type_id::create("sqr", this);
            drv = alu_driver::type_id::create("drv", this);
        end
        mon = alu_monitor::type_id::create("mon", this);
    endfunction

    virtual function void connect_phase(uvm_phase phase);
        super.connect_phase(phase);
        if (is_active == UVM_ACTIVE) begin
            drv.seq_item_port.connect(sqr.seq_item_export);
        end
        ap = mon.ap;
    endfunction

    `uvm_component_utils(alu_agent)
endclass;

`endif
