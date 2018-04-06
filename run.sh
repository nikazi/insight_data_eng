#!/bin/bash

bazel build //:EDGAR
bazel-bin/EDGAR $1/input/inactivity_period.txt $1/input/log.csv $2/output/sessionization.txt