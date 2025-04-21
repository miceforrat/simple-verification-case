class counter_ref;
    bit [3:0] expected;
    bit [3:0] shadow;
    // 构造函数：初始化计数器为 0
    function new();
        expected = 4'b0;
        shadow = 4'b0;
    endfunction
    
    // 根据复位信号更新预期值
    function void update(bit rst);
        if (rst) begin
            expected = 4'b0;
            shadow = 4'b0;
        end else begin
            expected = shadow;
            shadow = shadow + 1;
        end
    endfunction
endclass
