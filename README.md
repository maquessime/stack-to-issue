# stack-to-issue #

[![Build Status](https://travis-ci.org/maquessime/stack-to-issue.svg?branch=master)](https://travis-ci.org/maquessime/stack-to-issue)

## Build & Run ##

```sh
$ cd stack-to-issue
$ ./start.sh
> container:start
```

## Normal Flow ##

```sh
#search issues from a stack
curl -d "your_stack" -H "Content-type: text/plain;charset=UTF-8" http://localhost:8080
{"hash":"080c4480023c2000006801a8000090000003050208402004011c0110038098ca","issues":[]}

#insert issue for your stack
curl -X POST -d '{"issues":["BUG1]}' http://localhost:8080/stacks/080c4480023c2000006801a8000090000003050208402004011c0110038098ca/issues
curl -d "your_stack" -H "Content-type: text/plain;charset=UTF-8" http://localhost:8080
{"hash":"080c4480023c2000006801a8000090000003050208402004011c0110038098ca","issues":["BUG1"]}

#add an issue for your stack
curl -X POST -d '{"issues":["BUG2"]} http://localhost:8080/stacks/080c4480023c2000006801a8000090000003050208402004011c0110038098ca/issues
curl -d "your_stack" -H "Content-type: text/plain;charset=UTF-8" http://localhost:8080
{"hash":"080c4480023c2000006801a8000090000003050208402004011c0110038098ca","issues":["BUG1","BUG2"]}

#delete a stack
curl -X DELETE http://localhost:8080/stacks/080c4480023c2000006801a8000090000003050208402004011c0110038098ca/
```
