`timescale 1ns/1ps

`include "uvm_pkg.sv"
import uvm_pkg::*;

`include "alu_ports/alu_if.sv"
`include "src/env/alu_env.sv"
`include "src/testcase/base_test.sv"
`include "src/testcase/test_random.sv"

module top_tb;
    reg clock;

    alu_if dut_if(clock);
    ALU alu(
        .a(dut_if.a),
        .b(dut_if.b),
        .alu_sel(dut_if.sel),
        .alu_out(dut_if.out)
    );

    /* Start UVM*/
    initial begin
        run_test();
    end

    /* Configuration */
    initial begin
        uvm_config_db#(virtual alu_if)::set(null, "uvm_test_top.env.alu_agent.drv", "aif", dut_if);
        uvm_config_db#(virtual alu_if)::set(null, "uvm_test_top.env.alu_agent.mon", "aif", dut_if);
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
        $dumpfile("alu.vcd");
        $dumpvars(0, top_tb);
    end
endmodule
