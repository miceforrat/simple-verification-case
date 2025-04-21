`ifndef FIFO_IF
`define FIFO_IF

interface fifo_if(input clock);
		
	logic rst;
	logic wr_en;          //写使能
	logic rd_en;          //读使能
	logic [7:0] wdata;     //写入数据输入
	logic [7:0] rdata;    //读取数据输出
	logic empty;         //读空标志信号
	logic full;         //写满标志信号

	logic op;

endinterface //fifo_if

`endif