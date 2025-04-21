`ifndef COUNTER_SCOREBOARD__SV
`define COUNTER_SCOREBOARD__SV

`include "counter_ports/counter_transaction.sv"
`include "src/env/counter_ref.sv"

class counter_scoreboard extends uvm_scoreboard;
    // 分析接口，用于接收 Monitor 传递过来的事务
    uvm_analysis_imp #(counter_transaction, counter_scoreboard) ap;
    counter_ref ref_model;
    
    `uvm_component_utils(counter_scoreboard)
    
    function new(string name, uvm_component parent);
        super.new(name, parent);
        ap = new("ap", this);
        ref_model = new();
    endfunction
    
    // write() 方法在接收到每个事务时更新参考模型并进行比对
    virtual function void write(counter_transaction trans);
        // 更新参考模型：若 rst 为 1，则计数器清零，否则累加
        ref_model.update(trans.rst);
        
        // 比对 DUT 输出和参考模型计算的预期值
        if (trans.count !== ref_model.expected) begin
            `uvm_error("COUNTER_SCOREBOARD", 
                $sformatf("Mismatch! Expected count: %0h, Got: %0h", ref_model.expected, trans.count));
        end else begin
            `uvm_info("COUNTER_SCOREBOARD", "Count matches expected value", UVM_MEDIUM);
        end
    endfunction
endclass

`endif
