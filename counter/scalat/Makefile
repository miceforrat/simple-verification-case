VSRC_DIR    = ../vsrc
PICKER      = picker
VSRC_NAME   = counter
TOP_MODULE  = Counter
SIM         = verilator
WAVE_FORMAT = fst
TARGET_LANG = scala
NAME_L = $(shell echo $(TOP_MODULE) | tr A-Z a-z)
LIB_DIR = lib
SHELL := /bin/bash

dut:
	if [ ! -d "./$(LIB_DIR)" ]; then \
		mkdir $(LIB_DIR); \
	fi

	if [ -d "./picker_out_$(NAME_L)" ]; then \
		rm -r picker_out_$(NAME_L); \
	fi

	$(PICKER) export --autobuild=true $(VSRC_DIR)/$(VSRC_NAME).v \
		-w $(TOP_MODULE).$(WAVE_FORMAT) \
		--sname $(TOP_MODULE) \
		--tdir picker_out_$(NAME_L)/UT_$(TOP_MODULE) \
		--lang $(TARGET_LANG) \
		--sim $(SIM) \
		-e -c
	cp -r picker_out_$(NAME_L)/UT_$(TOP_MODULE)/*.jar ./$(LIB_DIR)

run:
	sbt -mem 12288 test

clean:
	rm -rf picker_out_$(NAME_L)