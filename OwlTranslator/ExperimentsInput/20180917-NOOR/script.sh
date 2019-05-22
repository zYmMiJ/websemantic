#!/bin/bash

. params.sh

for op in ${OPS}
do
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80 java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-gen java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -Dgenerative
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-gen-empty java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -Dgenerative -Dstartempty
   bash scripts/runexp.sh -p ${OUTPUT} -d ${DIRPREF}-${op}-clever-nr-im80-gen-empty java -Dlog.level=INFO -cp lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:. fr.inria.exmo.lazylavender.engine.Monitor ${OPT} ${LOADOPT} -DrevisionModality=${op} -DexpandAlignments=clever -DnonRedundancy -DimmediateRatio=80 -Dgenerative -Dstartempty
done

