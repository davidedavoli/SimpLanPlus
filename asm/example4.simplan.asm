push 0
mv $sp $fp //Load new $fp
push $fp
li $a0 1

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function5// jump to start of function and put in $ra next istruction

push $a0

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function5
Function5:
mv $sp $fp
push $ra
li $a0 1

push $a0

//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.CallExpNode
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id
push $a0 // push e1
push $fp
mv $fp $al //put in $al actual fp
push $al
jal  Function6// jump to start of function and put in $ra next istruction
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

print $a0

//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.CallExpNode
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id
push $a0 // push e1
push $fp
mv $fp $al //put in $al actual fp
push $al
jal  Function6// jump to start of function and put in $ra next istruction
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction5

//CREO FUNZIONI
//BEGIN FUNCTION Function6
Function6:
mv $sp $fp
push $ra
li $a0 0

push $a0

li $a0 1

push $a0

//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.IdExpNode
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id
push $a0 // push e1
mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction6


endFunction6:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF uno

//FINE FUNZIONI

endFunction5:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF successivo

//FINE FUNZIONI
