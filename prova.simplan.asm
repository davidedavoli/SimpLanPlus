push 0
sw $sp 0($fp)
subi $sp $sp 1 // non assegnato nulla

li $a0 6

push $a0

//Start codegen of ast.node.types.IntTypeNode<ast.node.types.IntTypeNode
lw $a1 0($fp) //put in $a1 (al) actual fp
lw $a0 -2($a1) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
lt $a0 $a2 $a0 // $a0 = $a2 < $a0
push $a0

//Start codegen of ast.node.types.BoolTypeNode==ast.node.types.BoolTypeNode
li $a0 1
push $a0 // push e1
li $a0 0
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0
bc $a0 LABELthen1
li $a0 6

print $a0
b LABELendIf2
LABELthen1:
li $a0 7

print $a0

LABELendIf2:

lw $a1 0($fp) //put in $a1 (al) actual fp
lw $a0 -2($a1) //put in $a0 value of Id

lw $a1 0($fp) //put in $a1 (al) actual fp
addi $a1 $a1 -1 //put in $a1 address of Id

sw $a0 0($a1) // 0($a1) = $a0 id=exp 

//Start codegen of ast.node.types.IntTypeNode-ast.node.types.IntTypeNode
lw $a1 0($fp) //put in $a1 (al) actual fp
lw $a0 -2($a1) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

lw $a1 0($fp) //put in $a1 (al) actual fp
addi $a1 $a1 -2 //put in $a1 address of Id

sw $a0 0($a1) // 0($a1) = $a0 id=exp 


halt
