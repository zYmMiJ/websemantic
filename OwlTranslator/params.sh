# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=d230f8c69b7c97c4c570785e554aabcb2bbe09e1

# Experiment parameters
DESIGNDATE=2018-06-01
DATE=20181112
LABEL=${DATE}-NOOR
NAME=${LABEL}
PERFORMER=JEuz
NBAGENTS=4
NBITERATIONS=10000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"
PARAMS="-Dstrengthen=random"
postfix=strrdm

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

DIRPREF=${NBAGENTS}-${NBITERATIONS}
TOCOPY="expeRun logback.xml"

# Documentation parameters

VARIATIONOF=20180828-NOOR

EXPE="Revision of networks of ontologies with random strenghening"

HYPOTHESIS="The random version of strengthening does not differ much from the most specific and most general ones"

SETTING="Repeating [[${VARIATIONOF}]] replaying the runs of [[20180308-NOOR]] with random strengthening"

# Would be better automated
CLASSES="NOOEnvironment, AlignmentAdjustingAgent, AlignmentRevisionExperiment, ActionLogger, AverageLogger, Monitor"

# DEFAULT VALUES
DESIGNER="Iris Lohja"
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}


