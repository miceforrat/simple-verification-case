class alu_ref;

  // 静态函数：calc
  // 输入a、b均为8位数据，sel为4位选择信号
  static function logic [7:0] calc(
    input logic [7:0] a,
    input logic [7:0] b,
    input logic [3:0] sel
  );
    logic [7:0] result;
    begin
      case (sel)
        4'b0000: result = a + b;                            // 加法
        4'b0001: result = a - b;                            // 减法
        4'b0010: result = a * b;                            // 乘法
        4'b0011: result = (b != 8'd0) ? a / b : 8'd0;         // 除法（除数为0时返回0）
        4'b0100: result = a << 1;                           // 逻辑左移
        4'b0101: result = a >> 1;                           // 逻辑右移
        4'b0110: result = {a[6:0], a[7]};                   // 循环左移（Rotate left）
        4'b0111: result = {a[0], a[7:1]};                   // 循环右移（Rotate right）
        4'b1000: result = a & b;                            // 按位与
        4'b1001: result = a | b;                            // 按位或
        4'b1010: result = a ^ b;                            // 按位异或
        4'b1011: result = ~(a | b);                         // 按位或取反（NOR）
        4'b1100: result = ~(a & b);                         // 按位与取反（NAND）
        4'b1101: result = ~(a ^ b);                         // 按位异或取反（XNOR）
        4'b1110: result = (a > b) ? 8'd1 : 8'd0;             // 大于比较
        4'b1111: result = (a == b) ? 8'd1 : 8'd0;            // 等于比较
        default: result = a + b;                            // 默认采用加法
      endcase
      return result;
    end
  endfunction

endclass
