`ifndef TEST_RANDOM__SV
`define TEST_RANDOM__SV

class test_random_sequence extends uvm_sequence #(alu_transaction);
    `uvm_object_utils(test_random_sequence)

    alu_transaction tr;

    function new(string name = "test_random");
        super.new(name);
    endfunction

    virtual task body();
        // if (starting_phase != null)
        //     starting_phase.raise_objection(this);
        
        
	    // for (int sel = 0; sel < 16; sel++) begin
        //     for (int a = 0; a < 256; a++) begin
        //         for (int b = 0; b < 256; b++) begin
        //             `uvm_info(get_type_name(), $sformatf("Generating transaction %0d", sel * a * b), UVM_MEDIUM)
        //     		`uvm_do_with(tr, {tr.sel == sel; tr.a == a; tr.b == b;})
        // 	    end
        //     end

        // 	// for (int i = 0; i < 2000; i++) begin
        //     // 		`uvm_info(get_type_name(), $sformatf("Generating transaction %0d", i), UVM_MEDIUM)
        //     // 		`uvm_do_with(tr, {tr.sel == sel;})
        // 	// end
	    // end
        int finished_count = 0;
        event done_event;

        begin

            if (starting_phase != null)
                starting_phase.raise_objection(this);

            for (int sel = 0; sel < 16; sel++) begin

                automatic int local_sel = sel;
                fork
                    begin
                        run_one_sel(local_sel);
                        finished_count++;
                        ->done_event;  // 触发完成信号
                    end
                join_none
            end

            // 等待所有 16 个任务完成
            wait (finished_count == 16);

            if (starting_phase != null)
                starting_phase.drop_objection(this);
        end

        // #10000
        // if (starting_phase != null)
        //     starting_phase.drop_objection(this);
    endtask


    task run_one_sel(int sel);
        for (int a = 0; a < 256; a++) begin
            for (int b = 0; b < 256; b++) begin
                alu_transaction tr;
                `uvm_do_with(tr, {
                    tr.sel == sel;
                    tr.a == a;
                    tr.b == b;
                })
                // uvm_info(get_type_name(),
                //           $sformatf("sel=%0d, a=%0d, b=%0d", sel, a, b),
                //           UVM_LOW)
            end
        end
    endtask
endclass


class test_random extends base_test;
    `uvm_component_utils(test_random)

    function new(string name = "test_random", uvm_component parent = null);
        super.new(name, parent);
    endfunction

    virtual function void build_phase(uvm_phase phase);
        super.build_phase(phase);
        uvm_config_db#(uvm_object_wrapper)::set(this,
                                                "env.alu_agent.sqr.main_phase",
                                                "default_sequence",
                                                test_random_sequence::type_id::get());
    endfunction
endclass

`endif
