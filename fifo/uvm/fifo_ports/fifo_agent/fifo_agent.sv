`ifndef FIFO_AGENT__SV
`define FIFO_AGENT__SV

`include "fifo_ports/fifo_agent/fifo_sequencer.sv"
`include "fifo_ports/fifo_agent/fifo_driver.sv"
`include "fifo_ports/fifo_agent/fifo_monitor.sv"

class fifo_agent extends uvm_agent;
    fifo_sequencer sqr;
    fifo_driver drv;
    fifo_monitor mon;

    uvm_analysis_port #(fifo_transaction) ap;

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);

        if (is_active == UVM_ACTIVE) begin
            `uvm_info(get_type_name(), "ACTIVE", UVM_MEDIUM)
            sqr = fifo_sequencer::type_id::create("sqr", this);
            drv = fifo_driver::type_id::create("drv", this);
        end
        mon = fifo_monitor::type_id::create("mon", this);
    endfunction

    virtual function void connect_phase(uvm_phase phase);
        super.connect_phase(phase);
        if (is_active == UVM_ACTIVE) begin
            drv.seq_item_port.connect(sqr.seq_item_export);
        end
        ap = mon.ap;
    endfunction

    `uvm_component_utils(fifo_agent)
endclass;

`endif
