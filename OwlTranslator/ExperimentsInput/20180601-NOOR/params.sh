# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=d50e70f87bca76951ec2f149dc8ae1d42b9a1a28

# Experiment parameters
DATE=20180723
LABEL=20180601-NOOR
NAME=${LABEL}
PERFORMER=euzenat
NBAGENTS=4
NBITERATIONS=10000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"
MODS="-clever-nr -clever-nr-im80"
postfix=-gen

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

DIRPREF=${NBAGENTS}-${NBITERATIONS}

# Documentation parameters

VARIATIONOF=20170530-NOOR

EXPE="Revision of networks of ontologies with expansion, relaxation and generation starting with random network"

OPLIST=`echo $OPS | sed "s: :/:g"`

TITLE="${EXPE} (${NBAGENTS} agents; ${NBRUNS} runs; ${NBITERATIONS} games; ${OPLIST})"

HYPOTHESIS="Generating correspondences when needed should increase results quality (recall) with respect to expansion"

SETTING="Same as [[${VARIATIONOF}]] replaying the same runs as [[20180308-NOOR]] and after fixing addjoin and expansion."

# DEFAULT VALUES
DESIGNER=euzenat
EXPERIMENTER=${PERFORMER}
ANALYST=${PERFORMER}

