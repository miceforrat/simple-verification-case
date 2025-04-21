`ifndef FIFO_ENV__SV
`define FIFO_ENV__SV

`include "fifo_ports/fifo_agent/fifo_agent.sv"
`include "src/env/fifo_scoreboard.sv"

class fifo_env extends uvm_env;
    `uvm_component_utils(fifo_env)

    fifo_agent fifo_ag;
    fifo_scoreboard scoreboard;

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);

        fifo_ag = fifo_agent::type_id::create("fifo_agent", this);
        scoreboard = fifo_scoreboard::type_id::create("scoreboard", this);
    endfunction

    virtual function void connect_phase(uvm_phase phase);
        super.connect_phase(phase);

        fifo_ag.ap.connect(scoreboard.ap);
    endfunction

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction
endclass

`endif
