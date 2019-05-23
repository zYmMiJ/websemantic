# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=619edd1134aa281afa1a5dc101ac541be5f021f4

# Experiment parameters
DESIGNDATE=2018-06-01
DATE=20181109
LABEL=${DATE}-NOOR
NAME=${LABEL}
PERFORMER=JEuz
NBAGENTS=4
NBITERATIONS=10000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"
PARAMS="-Dstrengthen=mostspecific"
postfix=strspc

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

DIRPREF=${NBAGENTS}-${NBITERATIONS}
TOCOPY="expeRun logback.xml"

# Documentation parameters

VARIATIONOF=20180828-NOOR

EXPE="Revision of networks of ontologies with most specific strenghening (improved code)"

HYPOTHESIS="Strenghening improves recall over expansion"

SETTING="Repeating [[${VARIATIONOF}]] replaying the runs of [[20180308-NOOR]] with most specific strengthening on a reengineered strenghening code"

# Would be better automated
CLASSES="NOOEnvironment, AlignmentAdjustingAgent, AlignmentRevisionExperiment, ActionLogger, AverageLogger, Monitor"

# DEFAULT VALUES
DESIGNER="Iris Lohja"
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}

