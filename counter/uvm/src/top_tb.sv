`timescale 1ns/1ps

`include "uvm_pkg.sv"
import uvm_pkg::*;

`include "counter_ports/counter_if.sv"
`include "src/env/counter_env.sv"
`include "src/testcase/base_test.sv"
`include "src/testcase/test_random.sv"

module top_tb;
    reg clock;

    counter_if dut_if(clock);
    Counter counter(
        .clk(dut_if.clock),
        .rst(dut_if.rst),
        .count(dut_if.count)
    );

    /* Start UVM*/
    initial begin
        run_test();
    end

    /* Configuration */
    initial begin
        uvm_config_db#(virtual counter_if)::set(null, "uvm_test_top.env.counter_agent.drv", "cif", dut_if);
        uvm_config_db#(virtual counter_if)::set(null, "uvm_test_top.env.counter_agent.mon", "cif", dut_if);
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
        $dumpfile("counter.vcd");
        $dumpvars(0, top_tb);
    end
endmodule
