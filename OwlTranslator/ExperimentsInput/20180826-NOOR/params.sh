# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=759ff097b96520c12aa84f3749927f9a22022e62

# Experiment parameters
DESIGNDATE=2018-05-03
DATE=20180826
LABEL=${DATE}-NOOR
NAME=${LABEL}
PERFORMER=JEuz
NBAGENTS=4
NBITERATIONS=2000
NBRUNS=10
OUTPUT=${LABEL}
OPS="add addjoin"

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec -Drealistic"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"
TOCOPY="logback.xml"

# Incorrectly labelled with a 0 in the end (only 1000 runs)
DIRPREF=${NBAGENTS}-${NBITERATIONS}

# Documentation parameters

VARIATIONOF=20180305-NOOR

EXPE="Revision of networks of ontologies with most specific strenghening"

HYPOTHESIS="Addjoin achieves the same as add but faster"

SETTING="Same as [[${VARIATIONOF}]] after correction of expansion..."

# Would be better automated
CLASSES="NOOEnvironment, AlignmentAdjustingAgent, AlignmentRevisionExperiment, ActionLogger, AverageLogger, Monitor"

# DEFAULT VALUES
DESIGNER=${PERFORMER}
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}


