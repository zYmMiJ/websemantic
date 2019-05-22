# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=759ff097b96520c12aa84f3749927f9a22022e62

# Experiment parameters
DESIGNDATE=2017-10-01
DATE=20180917
LABEL=${DATE}-NOOR
NAME=${LABEL}
PERFORMER=JEuz
NBAGENTS=5
NBITERATIONS=50000
NBRUNS=10
OUTPUT=${LABEL}
OPS="delete replace refine add addjoin refadd"

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"
TOCOPY="expeRun logback.xml"

# Incorrectly labelled with a 0 in the end (only 1000 runs)
DIRPREF=${NBAGENTS}-${NBITERATIONS}

# Documentation parameters

VARIATIONOF=20171128-NOOR

EXPE="Revision of networks of ontologies with 5 agents relaxation, expansion and generation"

HYPOTHESIS="Threads observed with four agents are the same after fixing expansion and add behaviour"

SETTING="Same as [[${VARIATIONOF}]] after correction of expansion and add behaviour"

# Would be better automated
CLASSES="NOOEnvironment, AlignmentAdjustingAgent, AlignmentRevisionExperiment, ActionLogger, AverageLogger, Monitor"

# DEFAULT VALUES
DESIGNER=${PERFORMER}
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}


