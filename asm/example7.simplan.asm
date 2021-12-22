push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // No value assigned

li $a0 2

push $a0

//Start codegen of ast.node.exp.BinExpNode*ast.node.exp.single_exp.IntNode
//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id c
push $a0 // push e1
li $a0 3
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0
push $a0 // push e1
li $a0 2
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
mult $a0 $a2 $a0 // a0 = t1+a0

push $a0

//Start codegen of ast.node.exp.BinExpNode*ast.node.exp.single_exp.IntNode
//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id z
push $a0 // push e1
li $a0 2
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0
push $a0 // push e1
li $a0 2
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
mult $a0 $a2 $a0 // a0 = t1+a0

push $a0

mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id z

print $a0

mv $fp $al //put in $al actual fp
lw $a0 -4($al) //put in $a0 value of Id f

print $a0

halt
