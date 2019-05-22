# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=759ff097b96520c12aa84f3749927f9a22022e62

# Experiment parameters
DESIGNDATE=2018-06-01
DATE=20180829
LABEL=${DATE}-NOOR
NAME=${LABEL}
PERFORMER=JEuz
NBAGENTS=4
NBITERATIONS=10000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"
PARAMS="-Dstrengthen=mostgeneral"
postfix=strgen

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

DIRPREF=${NBAGENTS}-${NBITERATIONS}
TOCOPY="expeRun logback.xml"

# Documentation parameters

VARIATIONOF=20180601-NOOR

EXPE="Revision of networks of ontologies with most general strenghening"

HYPOTHESIS="Strenghening improves recall over expansion"

SETTING="Same as [[${VARIATIONOF}]] replaying the same runs as [[20180308-NOOR]] with most general strengthening."

# Would be better automated
CLASSES="NOOEnvironment, AlignmentAdjustingAgent, AlignmentRevisionExperiment, ActionLogger, AverageLogger, Monitor"

# DEFAULT VALUES
DESIGNER="Iris Lohja"
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}


