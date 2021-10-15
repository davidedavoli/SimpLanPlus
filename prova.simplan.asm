push 0
mv $sp $fp //Load new $fp
push $fp
li $a0 8

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next istruction

print $a0

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
//Start codegen of ast.node.types.IntTypeNode==ast.node.types.IntTypeNode
mv $fp $al //put in $a1 (al) actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 0
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0

bc $a0 LABELthen1
b LABELendIf2
LABELthen1:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 0

lw $fp 0($fp) //Load old $fp pushed 
subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:

//Start codegen of ast.node.types.IntTypeNode==ast.node.types.IntTypeNode
mv $fp $al //put in $a1 (al) actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0

bc $a0 LABELthen3
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
push $fp
push $fp
//Start codegen of ast.node.types.IntTypeNode-ast.node.types.IntTypeNode
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 2
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function0// jump to start of function and put in $ra next istruction

push $a0
push $fp
//Start codegen of ast.node.types.IntTypeNode-ast.node.types.IntTypeNode
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function0// jump to start of function and put in $ra next istruction

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
push $al
jal  Function1// jump to start of function and put in $ra next istruction

lw $fp 0($fp) //Load old $fp pushed 
subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf4
LABELthen3:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 1

lw $fp 0($fp) //Load old $fp pushed 
subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf4:

//CREO FUNZIONI
//BEGIN FUNCTION Function1
Function1:
mv $sp $fp
push $ra
//Start codegen of ast.node.types.IntTypeNode==ast.node.types.IntTypeNode
mv $fp $al //put in $a1 (al) actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 0
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0

bc $a0 LABELthen5
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
//Start codegen of ast.node.types.IntTypeNode+ast.node.types.IntTypeNode
li $a0 1
push $a0 // push e1
push $fp
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
lw $a0 2($al) //put in $a0 value of Id

push $a0
//Start codegen of ast.node.types.IntTypeNode-ast.node.types.IntTypeNode
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function1// jump to start of function and put in $ra next istruction
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

lw $fp 0($fp) //Load old $fp pushed 
subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction1

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf6
LABELthen5:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
lw $a0 2($al) //put in $a0 value of Id

lw $fp 0($fp) //Load old $fp pushed 
subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction1

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf6:


endFunction1:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 2// pop parameters2
pop
lw $fp 0($sp)
pop
jr $ra
// END OF sum

//FINE FUNZIONI

endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF fibonacci

//FINE FUNZIONI
