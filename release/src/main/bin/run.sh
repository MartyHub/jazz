#!/bin/sh

# where this script live
SCRIPT_DIR=$(cd $(dirname $0) && pwd)

# assume the application dir is one level up from script dir
APP_DIR=$(cd $SCRIPT_DIR/.. && pwd)

JAVA_HOME=${JAVA_HOME:=}
RUN_JAVA_OPTS=${JAZZ_JAVA_OPTS:=}

# set the java executable
JAVA_CMD=java

if [ -d "$JAVA_HOME" ]; then
	JAVA_CMD="$JAVA_HOME/bin/java"
fi

# search for java args
ARGS="$@"

NEW_ARGS[0]=''
IDX=0

for ARG in "$@"; do
    case $ARG in
	'-D'*)
		RUN_JAVA_OPTS="$RUN_JAVA_OPTS $ARG"
		;;
	*)
		NEW_ARGS[$IDX]="$ARG"
		let IDX=$IDX+1
		;;
	esac
done

ARGS="${NEW_ARGS[@]}"

# set java classpath
RUN_JAVA_CP="$APP_DIR/etc:$APP_DIR/lib/*"

# display full java command
echo "$JAVA_CMD" $RUN_JAVA_OPTS -cp "$RUN_JAVA_CP" $ARGS

# run java command
"$JAVA_CMD" $RUN_JAVA_OPTS -cp "$RUN_JAVA_CP" org.jboss.weld.environment.se.StartMain $ARGS
