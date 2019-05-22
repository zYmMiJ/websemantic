# Parameters describing a LazyLav execution

# Execution parameters

NBAGENTS=4
NBITERATIONS=10000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"

JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-1.2.3.jar:lib/slf4j/logback-core-1.2.3.jar:.

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec -DimmediateRatio=80" 

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

DIRPREF=${NBAGENTS}-${NBITERATIONS}

