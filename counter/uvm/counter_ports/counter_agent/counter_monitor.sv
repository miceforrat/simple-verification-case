`ifndef COUNTER_MONITOR__SV
`define COUNTER_MONITOR__SV

`include "counter_ports/counter_transaction.sv"

class counter_monitor extends uvm_monitor;
    `uvm_component_utils(counter_monitor)
    virtual counter_if cif;
    uvm_analysis_port #(counter_transaction) ap;

    function new(string name = "counter_monitor", uvm_component parent = null);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);
        if (!uvm_config_db#(virtual counter_if)::get(this, "", "cif", cif))
            `uvm_fatal("counter_monitor", "No virtual interface set up.")
        ap = new("ap", this);
    endfunction

    virtual task main_phase(uvm_phase phase);
        while(1) begin
            collect_one_pkt();
        end
    endtask

    task collect_one_pkt();
        counter_transaction tr = new("tr");

        @(posedge cif.clock);

        tr.rst   = cif.cb.rst;
        tr.count = cif.cb.count;
        
        ap.write(tr);

    endtask
endclass

`endif
