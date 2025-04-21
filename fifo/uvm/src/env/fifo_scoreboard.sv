`ifndef FIFO_SCOREBOARD_SV
`define FIFO_SCOREBOARD_SV

`include "fifo_ports/fifo_transaction.sv"
`include "src/env/fifo_ref.sv" // 包含 fifo_ref_model_with_delay 定义

class fifo_scoreboard extends uvm_scoreboard;
  // 分析接口，用于接收 Monitor 传递过来的事务
  uvm_analysis_imp#(fifo_transaction, fifo_scoreboard) ap;
  fifo_ref ref_model;
  
  `uvm_component_utils(fifo_scoreboard)
  
  function new(string name, uvm_component parent);
    super.new(name, parent);
    ap = new("ap", this);
    ref_model = new();
  endfunction
  
  // write() 方法在每个时钟周期更新参考模型并进行比对
  virtual function void write(fifo_transaction trans);
    // 如果复位有效（假设 rst 为低电平有效），则重置参考模型
    if (trans.cmd == 1) begin
        ref_model.write(trans.wdata);
        if (trans.full !== ref_model.full || trans.empty != ref_model.empty) begin
        `uvm_error("FIFO_SCOREBOARD", 
                    $sformatf("FIFO write mismatch! Expected: empty: %0h, full: %0h; Got: empty: %0h, full: %0h", 
                    ref_model.empty, ref_model.full , trans.empty, trans.full));
        end

    end else begin
        ref_model.read();
        if (trans.full !== ref_model.full || trans.empty != ref_model.empty || trans.rdata != ref_model.read_res) begin
        `uvm_error("FIFO_SCOREBOARD", 
                    $sformatf("FIFO write mismatch! Expected: empty: %0h, full: %0h, rdata: %0h; Got: empty: %0h, full: %0h, rdata: %0h", 
                    ref_model.empty, ref_model.full, ref_model.read_res, trans.empty, trans.full, trans.rdata));
        end
    end

  endfunction
endclass

`endif
