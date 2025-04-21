`ifndef ALU_SCOREBOARD__SV
`define ALU_SCOREBOARD__SV

`include "alu_ports/alu_transaction.sv"
`include "src/env/alu_ref.sv"

class alu_scoreboard extends uvm_scoreboard;
    // 分析接口，用于接收 Monitor 传递过来的事务
    uvm_analysis_imp #(alu_transaction, alu_scoreboard) ap;
    alu_ref ref_model;
    
    `uvm_component_utils(alu_scoreboard)
    
    function new(string name, uvm_component parent);
        super.new(name, parent);
        ap = new("ap", this);
        ref_model = new();
    endfunction
    
    // write() 方法在接收到每个事务时更新参考模型并进行比对
    virtual function void write(alu_transaction trans);
        // 更新参考模型：若 rst 为 1，则计数器清零，否则累加

        bit [7:0] ref_out;
        ref_out = ref_model.calc(trans.a, trans.b, trans.sel);
        
        // 比对 DUT 输出和参考模型计算的预期值
        if (trans.out != ref_out) begin
            `uvm_error("ALU_SCOREBOARD", 
                $sformatf("Mismatch! Expected count: %0h, Got: %0h", ref_out, trans.out));
        end else begin
            `uvm_info("ALU_SCOREBOARD", "Count matches expected value", UVM_MEDIUM);
        end
    endfunction
endclass

`endif
