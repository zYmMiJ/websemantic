#!/bin/bash

. params.sh

# Dry test only for generating the games (the initial situation is the same as 20180917)
bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-nothing java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=nothing -DsaveDir=expeRun -DsaveGames -DloadDir=expeRun -DloadEnv -DloadAgents

for op in ${OPS}
do
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-gen java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -Dgenerative
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-gen-empty java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -Dgenerative -Dstartempty
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-empty java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -Dstartempty
done

# Dry test only for generating the realistic correspondences
bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-nothing2 java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=nothing -DsaveDir=expeRun -DloadGames -DloadDir=expeRun -DsaveEnv -DloadAgents

for op in ${OPS}
do
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-gen-real java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -Dgenerative -Drealistic
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-real java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -Drealistic
done

