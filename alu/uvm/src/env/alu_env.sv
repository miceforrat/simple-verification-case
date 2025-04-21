`ifndef ALU_ENV__SV
`define ALU_ENV__SV

`include "alu_ports/alu_agent/alu_agent.sv"
`include "src/env/alu_scoreboard.sv"

class alu_env extends uvm_env;
    `uvm_component_utils(alu_env)

    alu_agent alu_ag;
    alu_scoreboard scoreboard;

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);

        alu_ag = alu_agent::type_id::create("alu_agent", this);
        scoreboard = alu_scoreboard::type_id::create("scoreboard", this);
    endfunction

    virtual function void connect_phase(uvm_phase phase);
        super.connect_phase(phase);

        alu_ag.ap.connect(scoreboard.ap);
    endfunction

    function new(string name, uvm_component parent);
        super.new(name, parent);
    endfunction
endclass

`endif
