`ifndef ALU_MONITOR__SV
`define ALU_MONITOR__SV

`include "alu_ports/alu_transaction.sv"

class alu_monitor extends uvm_monitor;
    `uvm_component_utils(alu_monitor)
    virtual alu_if aif;
    uvm_analysis_port #(alu_transaction) ap;

    function new(string name = "alu_monitor", uvm_component parent = null);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);
        if (!uvm_config_db#(virtual alu_if)::get(this, "", "aif", aif))
            `uvm_fatal("alu_monitor", "No virtual interface set up.")
        ap = new("ap", this);
    endfunction

    virtual task main_phase(uvm_phase phase);
        while(1) begin
            collect_one_pkt();
        end
    endtask

    task collect_one_pkt();
        alu_transaction tr = new("tr");

        tr.a = aif.a;
        tr.b = aif.b;
        tr.sel = aif.sel;
        tr.out = aif.out;
        ap.write(tr);
        
        @(posedge aif.clock);

    endtask
endclass

`endif
