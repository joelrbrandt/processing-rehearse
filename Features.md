# Features for Processing-Rehearse #

## Live Editing ##
Edit code while the sketch is running, changes reflected in the sketch.
Auto-breakpoint on lines that contain errors

### Steps ###
  1. implement it

### Notes ###
  * Will require reloading of class when methods are added/deleted/signature changed (but that seems to be supported by BeanShell). There are issues with old instances of classes not having the right methods. Things could explode.
  * Will require a pretty robust parser to find errors and still continue to parse (different than BeanShell's?)

## Visualization of Lines that Execute ##
Color lines as they execute. Perhaps fade them out. Need some way of investigating order of execution after the fact. Also perhaps have a secondary demarcation for lines that have NEVER executed (poor man's coverage checking).

### Steps ###
  1. implement it

### Notes ###

## Print Points ##
Like breakpoints, but execution continues. A couple of features could be supported:
  * Snapshot program state and/or output. Allow browsing through output to investigate state
  * Mark a variable as a printpoint, get message every time the variable changes (by any line of code)
    * Perhaps demarcate times where a variable has been changed by a line of code that has never changed it before (in this execution)
    * Perhaps show how variable has changed over time (graph?)
    * Perhaps allow browsing through output and see variable value at those times

### Steps ###
  1. implement it

### Notes ###