VSRC_DIR    = ../vsrc
PICKER      = picker
VSRC_NAME   = alu
TOP_MODULE  = ALU
SIM         = verilator
WAVE_FORMAT = fst
TARGET_LANG = python
NAME_L = $(shell echo $(TOP_MODULE) | tr A-Z a-z)

run:
	pytest -sv . -n auto

dut:
	$(PICKER) export --autobuild=true $(VSRC_DIR)/$(VSRC_NAME).v \
		-w $(TOP_MODULE).$(WAVE_FORMAT) \
		--sname $(TOP_MODULE) \
		--tdir picker_out_$(NAME_L)/UT_$(TOP_MODULE) \
		--lang $(TARGET_LANG) \
		--sim $(SIM) \
		-e -c
	@cp -r picker_out_$(NAME_L)/UT_$(TOP_MODULE) .

stat:
	cloc --include-lang=Python ./*.py

clean:
	rm -rf UT_$(TOP_MODULE) picker_out_$(NAME_L) reports *.fst *.fst.hier *.dat
