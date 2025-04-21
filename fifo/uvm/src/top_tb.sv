`timescale 1ns/1ps

`include "uvm_pkg.sv"
import uvm_pkg::*;

`include "fifo_ports/fifo_if.sv"
`include "src/env/fifo_env.sv"
`include "src/testcase/base_test.sv"
`include "src/testcase/test_random.sv"

module top_tb;
    reg clock;

    fifo_if dut_if(clock);

    FIFO fifo(
        .clk(dut_if.clock),
        .rst_n(dut_if.rst),
        .wr_en(dut_if.wr_en),
        .rd_en(dut_if.rd_en),
        .wdata(dut_if.wdata),
        .rdata(dut_if.rdata),
        .empty(dut_if.empty),
        .full(dut_if.full)
    );

    /* Start UVM*/
    initial begin
        run_test();
    end

    /* Configuration */
    initial begin
        uvm_config_db#(virtual fifo_if)::set(null, "uvm_test_top.env.fifo_agent.drv", "fif", dut_if);
        uvm_config_db#(virtual fifo_if)::set(null, "uvm_test_top.env.fifo_agent.mon", "fif", dut_if);
    end

    /* Clock generation */
    initial begin
        clock = 0;
        forever begin
            #100 clock = ~clock;
        end
    end

    /* Dump waveform */
    initial begin
        $dumpfile("fifo.vcd");
        $dumpvars(0, top_tb);
    end
endmodule
