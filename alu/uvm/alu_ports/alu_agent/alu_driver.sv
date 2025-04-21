`ifndef ALU_DRIVER_SV
`define ALU_DRIVER_SV

`include "alu_ports/alu_transaction.sv"

class alu_driver extends uvm_driver#(alu_transaction);
    `uvm_component_utils(alu_driver)

    virtual alu_if aif;
    alu_transaction item;

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);
        if (!uvm_config_db#(virtual alu_if)::get(this, "", "aif", aif))
            `uvm_fatal("NO_AIF", "alu_if not found")
    endfunction

    virtual task run_phase(uvm_phase phase);
        forever begin
            seq_item_port.get_next_item(item);
            driver_one_pkt();
            seq_item_port.item_done();
        end
    endtask

    task driver_one_pkt();
        aif.a <= item.a;
        aif.b <= item.b;
        aif.sel <= item.sel;
        @(posedge aif.clock);
    endtask
endclass

`endif
