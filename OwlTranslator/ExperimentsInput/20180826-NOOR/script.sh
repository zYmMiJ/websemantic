#!/bin/bash
# see associated param.sh

. params.sh

# Dry test only for generating the runs
bash scripts/runexp.sh -d ${DIRPREF}-nothing java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=nothing -DsaveDir=expeRun -DsaveInit -DsaveParams -DsaveGames
bash scripts/runexp.sh -d ${DIRPREF}-add java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=add ${LOADOPT}
bash scripts/runexp.sh -d ${DIRPREF}-addjoin java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=addjoin ${LOADOPT}
bash scripts/runexp.sh -d ${DIRPREF}-add-syntactic java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=add -Dsyntactic=true ${LOADOPT} -DsaveDir=addRes -DsaveFinal
bash scripts/runexp.sh -d ${DIRPREF}-addjoin-syntactic java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=addjoin -Dsyntactic=true ${LOADOPT} -DsaveDir=addjoinRes -DsaveFinal

# Dry test only for generating the runs
bash scripts/runexp.sh -d ${DIRPREF}-real-nothing java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -Drealistic -DrevisionModality=nothing -DsaveDir=expeRun -DsaveInit -DsaveParams -DsaveGames
bash scripts/runexp.sh -d ${DIRPREF}-real-add java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -Drealistic -DrevisionModality=add ${LOADOPT}
bash scripts/runexp.sh -d ${DIRPREF}-real-addjoin java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -Drealistic -DrevisionModality=addjoin ${LOADOPT}
bash scripts/runexp.sh -d ${DIRPREF}-real-add-syntactic java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -Drealistic -DrevisionModality=add -Dsyntactic=true ${LOADOPT} -DsaveDir=addRes -DsaveFinal
bash scripts/runexp.sh -d ${DIRPREF}-real-addjoin-syntactic java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -Drealistic -DrevisionModality=addjoin -Dsyntactic=true ${LOADOPT} -DsaveDir=addjoinRes -DsaveFinal
