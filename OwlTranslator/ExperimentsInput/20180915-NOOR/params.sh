# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
CLASSPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=759ff097b96520c12aa84f3749927f9a22022e62

# Experiment parameters
DESIGNDATE=2018-08-31
DATE=20180915
LABEL=${DATE}-NOOR
NAME=${LABEL}
PERFORMER=JEuz
NBAGENTS=4
NBITERATIONS=10000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"
POSTFIX=gen-real
PARAMS="-Dgenerative -Drealistic"
OUTPUT=${LABEL}

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"
TOCOPY="expeRun logback.xml"

DIRPREF=${NBAGENTS}-${NBITERATIONS}

# Documentation parameters

VARIATIONOF=20180601-NOOR

EXPE="Revision of networks of ontologies with expansion, relaxation and generation starting with realistic alignment size"

HYPOTHESIS="Starting with realistic alignment size should get closer results to starting empty than non realistic size (expansion dominance)"
# It is unclear why such an hypothesis would hold, but it is worth considering

SETTING="Same as [[${VARIATIONOF}]] replaying the same runs as [[20180308-NOOR]] with realistic alignment size."

# Would be better automated
CLASSES="NOOEnvironment, AlignmentAdjustingAgent, AlignmentRevisionExperiment, ActionLogger, AverageLogger, Monitor"

# DEFAULT VALUES
DESIGNER=JEuz
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}


