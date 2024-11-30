VSRC_DIR    = ./vsrc
PICKER      = picker
VSRC_NAME   = fifo
TOP_MODULE  = FIFO
SIM         = verilator
WAVE_FORMAT = fst
TARGET_LANG = python
NAME_L = $(shell echo $(TOP_MODULE) | tr A-Z a-z)

run:
	pytest -sv .

dut:
	$(PICKER) export --autobuild=true $(VSRC_DIR)/$(VSRC_NAME).v \
		-w $(TOP_MODULE).$(WAVE_FORMAT) \
		--sname $(TOP_MODULE) \
		--tdir picker_out_$(NAME_L) \
		--lang $(TARGET_LANG) \
		--sim $(SIM) \
		-e -c
	@cp -r picker_out_$(NAME_L)/UT_$(TOP_MODULE) .

stat:
	cloc --include-lang=Python ./*.py

clean:
	rm -rf UT_$(TOP_MODULE) picker_out_$(NAME_L) reports *.fst *.fst.hier *.dat
