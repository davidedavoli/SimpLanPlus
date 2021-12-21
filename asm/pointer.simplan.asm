push 0
mv $sp $fp //Load new $fp
new $a0// put new address in a0

push $a0

new $a0// put new address in a0

push $a0

new $a0// put new address in a0

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 y=exp

li $a0 2

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) // de referencing inner
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 y=exp

//Start codegen of ast.node.exp.LhsExpNode+ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

lw $a0 0($a0)
lw $a0 0($a0)push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) // de referencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id y

lw $a0 0($a0)
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 x=exp

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id x

lw $a0 0($a0)
print $a0

halt
