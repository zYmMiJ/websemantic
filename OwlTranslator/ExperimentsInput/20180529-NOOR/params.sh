# Parameters describing a LazyLav execution
# Please preserve the order

# Environment parameters
OSVERS=stretch
LOGBACK_VERSION=1.2.3
JPATH=lib/lazylav/ll.jar:lib/slf4j/logback-classic-${LOGBACK_VERSION}.jar:lib/slf4j/logback-core-${LOGBACK_VERSION}.jar:.

LLHASH=d50e70f87bca76951ec2f149dc8ae1d42b9a1a28

# Experiment parameters
DATE=`date -Idate|sed "s:-::g"`
LABEL=20180529-NOOR
NAME=${LABEL}
PERFORMER=euzenat
NBAGENTS=4
NBITERATIONS=10000
NBRUNS=10
OPS="delete replace refine add addjoin refadd"

OPT="-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec"

# To be used when replaying
LOADOPT="-DloadDir=expeRun -DloadEnv -DloadAgents -DreplayGames"

DIRPREF=${NBAGENTS}-${NBITERATIONS}

# Documentation parameters

EXPE="Revision of networks of ontologies with expansion"

OPLIST=`echo $OPS | sed "s: :/:g"`

TITLE="${EXPE} (${NBAGENTS} agents; ${NBRUNS} runs; ${NBITERATIONS} games; ${OPLIST})"

HYPOTHESIS="expanding alignments when deleting correspondence will improve recall in the long run.
random/protected may not converge; clever will.
faster convergence can be obtained from random to clever-nr-comp.
the order between various operators is preserved.
precision is increased; recall preserved
"
SETTING="Same as [[20170216-NOOR]] replaying the same runs [[20180308-NOOR]] (putatively) and after fixing addjoin and expansion."

# DEFAULT VALUES
DESIGNER="euzenat (2017-02-16)"
EXPERIMENTER="${PERFORMER} (2018-05-29)"
ANALYST="${PERFORMER} (2018-08-16)"

