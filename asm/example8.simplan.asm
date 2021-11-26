push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // non assegnato nulla

subi $sp $sp 1 // non assegnato nulla

new $a0// put new address in a0

push $a0

li $a0 0

bc $a0 LABELthen11
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 8

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

li $a0 6

print $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf12
LABELthen11:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 5

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -3 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

li $a0 10

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -3 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 id=exp 

mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 -1($al) //put in $a0 value of Id

print $a0

li $a0 10

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -2 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf12:

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

li $a0 5

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 id=exp 

mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id

free $a0 //free address in $a0

new $a0// put new address in a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

li $a0 9

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 id=exp 

push $fp
mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id

lw $a0 0($a0)
push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function33// jump to start of function and put in $ra next istruction

push $fp
mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id

lw $a0 0($a0)
push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function34// jump to start of function and put in $ra next istruction

print $a0

mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function33
Function33:
mv $sp $fp
push $ra
li $a0 0

print $a0

//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

print $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction33


endFunction33:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF plusint

//BEGIN FUNCTION Function34
Function34:
mv $sp $fp
push $ra
li $a0 1

print $a0

//Start codegen of ast.node.exp.IdExpNode-ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction34


endFunction34:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF minusint

//FINE FUNZIONI
