#!/bin/bash

. params.sh

for op in delete replace refine add addjoin refadd
do
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-${POSTFIX} java -Dlog.level=INFO -cp ${CLASSPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy ${PARAMS}
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-${POSTFIX} java -Dlog.level=INFO -cp ${CLASSPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 ${PARAMS}
done
