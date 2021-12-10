push 0
mv $sp $fp //Load new $fp
li $a0 10

push $a0

//Start codegen of ast.node.exp.IdExpNode>=ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id a
push $a0 // push e1
li $a0 0
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
ge $a0 $a2 $a0 // $a0 = $a2 >= $a0

bc $a0 LABELthen1
li $a0 0

print $a0
b LABELendIf2
LABELthen1:
mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id a

print $a0

LABELendIf2:

halt
