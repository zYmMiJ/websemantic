#!/bin/bash
# Script to run experiments in bactch on a remore machine
# Procedure:
# - zip the current directory: cd ..; zip -rq lazylav.zip lazylav
# - transfer it on the execution machine: scp lazylav.zip seals-4.inrialpes.fr:/scratch/LazyLav/
# - unzip on this machine: unzip lazylav.zip; cd lazylav
# - run it there: nohup bash myscript.sh < /dev/null 2> /dev/null > /dev/null &
# - results are found the directories passed as -d

JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:.

OPT="-DnbAgents=4 -DnbIterations=10000 -DnbRuns=10 -DreportPrecRec"

# Dry test only for generating the runs
#bash scripts/runexp.sh -d 4-10000-nothing java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=nothing -DsaveDir=expeRun -DsaveInit -Drealistic

LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

for op in delete replace refine add addjoin refadd
do
   bash scripts/runexp.sh -d 4-10000-${op}-real java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op}
   bash scripts/runexp.sh -d 4-10000-${op}-im80-real java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DimmediateRatio=80
   bash scripts/runexp.sh -d 4-10000-${op}-clever-nr-real java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy
   bash scripts/runexp.sh -d 4-10000-${op}-clever-nr-im80-real java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80

done

exit


bash scripts/runexp.sh -d 4-10000-real-add java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=add ${LOADOPT}

bash scripts/runexp.sh -d 4-10000-real-addjoin java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=addjoin ${LOADOPT}

bash scripts/runexp.sh -d 4-10000-real-add-syntactic java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=add -Dsyntactic=true ${LOADOPT} -DsaveDir=addRes -DsaveFinal

bash scripts/runexp.sh -d 4-10000-real-addjoin-syntactic java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=addjoin -Dsyntactic=true ${LOADOPT} -DsaveDir=addjoinRes -DsaveFinal

exit

for op in delete replace refine add addjoin refadd
do
   # classic (+im80)
bash scripts/runexp.sh -d 4-10000-real-${op} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} ${LOADOPT}

bash scripts/runexp.sh -d 4-10000-real-${op}-im80 java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} ${LOADOPT} -DimmediateRatio=80

   # clever-nr (+im80)
bash scripts/runexp.sh -d 4-10000-real-${op}-clever-nr java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy

bash scripts/runexp.sh -d 4-10000-real-${op}-clever-nr-im80 java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80

   # clever-nr (+im80) + gen
bash scripts/runexp.sh -d 4-10000-real-${op}-clever-nr-gen java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -Dgenerative

bash scripts/runexp.sh -d 4-10000-real-${op}-clever-nr-im80-gen java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative

   # clever-nr (+im80) + gen +empty
bash scripts/runexp.sh -d 4-10000-real-${op}-clever-nr-gen-empty java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -Dgenerative -Dstartempty

bash scripts/runexp.sh -d 4-10000-real-${op}-clever-nr-im80-gen-empty java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -Dstartempty

done

exit

rep=logmap,alcomo

bash scripts/runexp.sh -d 4-10000-${op}-rr java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DrepeatRepairs=400 -Drepairers=${rep}

bash scripts/runexp.sh -d 4-10000-${op}-im80-rr java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DrepeatRepairs=400 -Drepairers=${rep} -DimmediateRatio=80

bash scripts/runexp.sh -d 4-10000-${op}-clever-nr-rr java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DrepeatRepairs=400 -Drepairers=${rep} -DexpandAlignments=clever -DnonRedundancy

bash scripts/runexp.sh -d 4-10000-${op}-clever-nr-im80-rr java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DrepeatRepairs=400 -Drepairers=${rep} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80

exit

for op in  delete replace refine add addjoin refadd
do

# basic
bash scripts/runexp.sh -d 4-10000-${op} java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op}

# im80
bash scripts/runexp.sh -d 4-10000-${op}-im80 java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DimmediateRatio=80

# clever-nr
bash scripts/runexp.sh -d 4-10000-${op}-clever-nr java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy

# clever-nr-complete
bash scripts/runexp.sh -d 4-10000-${op}-clever-nr-comp java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DontoLookup

# clever-nr-complete-im80
bash scripts/runexp.sh -d 4-10000-${op}-clever-nr-comp-im80 java -Dlog.level=INFO -cp ${JPATH} fr.inria.exmo.lazylavender.engine.Monitor ${OPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DontoLookup -DimmediateRatio=80

done


