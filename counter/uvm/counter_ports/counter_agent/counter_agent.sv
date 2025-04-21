`ifndef COUNTER_AGENT__SV
`define COUNTER_AGENT__SV

`include "counter_ports/counter_agent/counter_sequencer.sv"
`include "counter_ports/counter_agent/counter_driver.sv"
`include "counter_ports/counter_agent/counter_monitor.sv"

class counter_agent extends uvm_agent;
    counter_sequencer sqr;
    counter_driver drv;
    counter_monitor mon;

    uvm_analysis_port #(counter_transaction) ap;

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);

        if (is_active == UVM_ACTIVE) begin
            `uvm_info(get_type_name(), "ACTIVE", UVM_MEDIUM)
            sqr = counter_sequencer::type_id::create("sqr", this);
            drv = counter_driver::type_id::create("drv", this);
        end
        mon = counter_monitor::type_id::create("mon", this);
    endfunction

    virtual function void connect_phase(uvm_phase phase);
        super.connect_phase(phase);
        if (is_active == UVM_ACTIVE) begin
            drv.seq_item_port.connect(sqr.seq_item_export);
        end
        ap = mon.ap;
    endfunction

    `uvm_component_utils(counter_agent)
endclass;

`endif
