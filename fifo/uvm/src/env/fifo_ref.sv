class fifo_ref;
  localparam int FIFO_DEPTH = 8;
  // 内部队列用于模拟 FIFO 存储
  bit [7:0] fifo_q[$];
  // 延迟寄存器，用于模拟读操作的一个周期延迟
  bit [7:0] read_res;

  bit full;
  bit empty;

  // 构造函数
  function new();
    fifo_q.delete();
    read_res = 8'd0;
  endfunction

  function void write(bit[7:0] wdata);
    full = is_full();
    empty = is_empty();
    if (!is_full()) begin
      fifo_q.push_back(wdata);
    end
  endfunction

  function void read();
    if (!is_empty()) begin
      read_res = fifo_q.pop_front();
    end else begin
      read_res = 0;
    end
    full = is_full();
    empty = is_empty();
  endfunction


  function bit is_empty();
    return (fifo_q.size() == 0);
  endfunction

  function bit is_full();
    return (fifo_q.size() >= FIFO_DEPTH);
  endfunction

  // 重置参考模型
  function void reset();
    fifo_q.delete();
    read_res = 8'd0;
  endfunction

endclass
