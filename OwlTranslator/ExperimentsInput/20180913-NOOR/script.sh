#!/bin/bash

. params.sh

OUTPUT=${OUTPUT}${LABEL}

 # Dry test only for generating the games (the initial situation is the same as 20180828)
bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-nothing java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=nothing -DsaveDir=expeRun -DsaveGames -DloadDir=expeRun -DloadEnv -DloadAgents
   
for op in ${OPS}
do
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-strgen java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -Dstrengthen=mostgeneral
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-strspc java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -Dstrengthen=mostspecific
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-empty-strgen java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -DstartEmpty -Dstrengthen=mostgeneral
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-empty-strspc java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -DstartEmpty -Dstrengthen=mostspecific
done

