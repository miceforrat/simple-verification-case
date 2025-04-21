`ifndef COUNTER_IF
`define COUNTER_IF

interface counter_if(input clock);
    logic rst;
    logic [3:0]  count;

    clocking cb @(posedge clock);
        input rst;
        input count;
    endclocking

    modport mon (clocking cb);

endinterface //adder_if

`endif