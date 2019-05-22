#!/bin/bash

. params.sh

OUTPUT=${OUTPUT}${LABEL}

for op in ${OPS}
do
    bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-${postfix} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} ${PARAMS}
    bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-${postfix} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy ${PARAMS}
    bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-im80-${postfix} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DimmediateRatio=80 ${PARAMS}
    bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-${postfix} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 ${PARAMS}
    bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-${postfix} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative ${PARAMS}
    bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-empty-${postfix} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -DstartEmpty ${PARAMS}
done

