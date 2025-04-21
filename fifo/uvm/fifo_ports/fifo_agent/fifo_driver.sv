`ifndef FIFO_DRIVER_SV
`define FIFO_DRIVER_SV

`include "fifo_ports/fifo_transaction.sv"

class fifo_driver extends uvm_driver#(fifo_transaction);
    `uvm_component_utils(fifo_driver)

    virtual fifo_if fif;
    fifo_transaction item;

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);
        if (!uvm_config_db#(virtual fifo_if)::get(this, "", "fif", fif))
            `uvm_fatal("NO_FIF", "fifo_if not found")
    endfunction

    virtual task run_phase(uvm_phase phase);
        forever begin
            seq_item_port.get_next_item(item);
            if (item.rst) begin
                fif.rst <= item.rst;
            end else begin 
                if (item.cmd == 1'b1) begin
                    write();
                end else begin
                    read();
                end 
            end
            seq_item_port.item_done();
        end
    endtask


    task write();
        fif.wr_en <= 1'b1;
        fif.wdata <= item.wdata;

        @(posedge fif.clock)

        fif.wr_en <= 1'b0;
    endtask

    task read();

        fif.rd_en <= 1'b1;
        @(posedge fif.clock)
        fif.rd_en <= 1'b0;

    endtask

endclass

`endif
