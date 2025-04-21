`ifndef FIFO_MONITOR__SV
`define FIFO_MONITOR__SV

`include "fifo_ports/fifo_transaction.sv"

class fifo_monitor extends uvm_monitor;
    `uvm_component_utils(fifo_monitor)
    virtual fifo_if fif;
    uvm_analysis_port #(fifo_transaction) ap;

    function new(string name = "fifo_monitor", uvm_component parent = null);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);
        if (!uvm_config_db#(virtual fifo_if)::get(this, "", "fif", fif))
            `uvm_fatal("fifo_monitor", "No virtual interface set up.")
        ap = new("ap", this);
    endfunction

    virtual task main_phase(uvm_phase phase);
        while(1) begin
            collect_one_pkt();
        end
    endtask

    task collect_one_pkt();
        fifo_transaction tr = new("tr");
        @(posedge fif.clock);


        // 如果是写操作，只采集 full 和 empty（如果不需要采集 wdata，就不设置）
        if (fif.wr_en) begin
            tr.cmd   = 1;  // 假设你在 transaction 中定义了 cmd 字段
            @(posedge fif.clock);

            tr.empty = fif.empty;
            tr.full  = fif.full;
            tr.wdata = fif.wdata;
            ap.write(tr);
        end

        // 如果是读操作，等待一个 delta cycle 以确保 rdata 已更新，再采集 rdata、full 和 empty
        if (fif.rd_en) begin
            tr.cmd   = 0;   // 同样假设定义了 cmd 字段
            @(posedge fif.clock);

            tr.empty = fif.empty;
            tr.full  = fif.full;
            tr.rdata = fif.rdata;
            ap.write(tr);
        end

    endtask
endclass

`endif
