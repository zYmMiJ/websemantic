# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=759ff097b96520c12aa84f3749927f9a22022e62

# Experiment parameters
DESIGNDATE=2018-09-13
DATE=20180913
LABEL=${DATE}-NOOR
NAME=${LABEL}
PERFORMER=JEuz
NBAGENTS=4
NBITERATIONS=20000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

DIRPREF=${NBAGENTS}-${NBITERATIONS}
TOCOPY="expeRun logback.xml"

# Documentation parameters

VARIATIONOF=20180828-NOOR

EXPE="Revision of networks of ontologies with strenghening on longer span"

HYPOTHESIS="Strenghening with refadd, relaxation, expansion, generation reaches 100% recall"

SETTING="Same as [[${VARIATIONOF}]] on 20000 games"

# Would be better automated
CLASSES="NOOEnvironment, AlignmentAdjustingAgent, AlignmentRevisionExperiment, ActionLogger, AverageLogger, Monitor"

# DEFAULT VALUES
DESIGNER=JEuz
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}


