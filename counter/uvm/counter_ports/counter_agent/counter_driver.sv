`ifndef COUNTER_DRIVER_SV
`define COUNTER_DRIVER_SV

`include "counter_ports/counter_transaction.sv"

class counter_driver extends uvm_driver#(counter_transaction);
    `uvm_component_utils(counter_driver)

    virtual counter_if cif;
    counter_transaction item;

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);
        if (!uvm_config_db#(virtual counter_if)::get(this, "", "cif", cif))
            `uvm_fatal("NO_CIF", "counter_if not found")
    endfunction

    virtual task run_phase(uvm_phase phase);
        forever begin
            seq_item_port.get_next_item(item);
            driver_one_pkt();
            seq_item_port.item_done();
        end
    endtask

    task driver_one_pkt();
        cif.rst <= item.rst;

        @(posedge cif.clock);
    endtask
endclass

`endif
