push 0
mv $sp $fp //Load new $fp
push $fp
li $a0 20

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function4// jump to start of function and put in $ra next istruction

print $a0

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function4
Function4:
mv $sp $fp
push $ra
//Start codegen of ast.node.types.IntTypeNode<=ast.node.types.IntTypeNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
le $a0 $a2 $a0 // $a0 = $a2 <= $a0

bc $a0 LABELthen9
//Start codegen of ast.node.types.IntTypeNode+ast.node.types.IntTypeNode
push $fp
//Start codegen of ast.node.types.IntTypeNode-ast.node.types.IntTypeNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
push $al
jal  Function4// jump to start of function and put in $ra next istruction
push $a0 // push e1
push $fp
//Start codegen of ast.node.types.IntTypeNode-ast.node.types.IntTypeNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 2
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
push $al
jal  Function4// jump to start of function and put in $ra next istruction
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction4
b LABELendIf10
LABELthen9:
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction4

LABELendIf10:


endFunction4:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF fib

//FINE FUNZIONI
