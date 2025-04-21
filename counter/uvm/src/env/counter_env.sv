`ifndef COUNTER_ENV__SV
`define COUNTER_ENV__SV

`include "counter_ports/counter_agent/counter_agent.sv"
`include "src/env/counter_scoreboard.sv"

class counter_env extends uvm_env;
    `uvm_component_utils(counter_env)

    counter_agent counter_ag;
    counter_scoreboard scoreboard;

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);

        counter_ag = counter_agent::type_id::create("counter_agent", this);
        scoreboard = counter_scoreboard::type_id::create("scoreboard", this);
    endfunction

    virtual function void connect_phase(uvm_phase phase);
        super.connect_phase(phase);

        counter_ag.ap.connect(scoreboard.ap);
    endfunction

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction
endclass

`endif
