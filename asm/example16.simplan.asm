push 0
mv $sp $fp //Load new $fp
push $fp
mv $fp $al //put in $al actual fp
push $al
jal  Function6// jump to start of function and put in $ra next istruction

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
new $a0// put new address in a0

push $a0

new $a0// put new address in a0

push $a0

li $a0 1

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

li $a0 1

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -3 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 y=exp

push $fp
mv $fp $al //put in $al actual fp
lw $a0 -3($al) //put in $a0 value of Id y

push $a0
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id x

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function1// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0
//CREO FUNZIONI
//BEGIN FUNCTION Function1
Function1:
mv $sp $fp
push $ra
//Start codegen of ast.node.exp.LhsExpNode==ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 2($al) //put in $a0 value of Id y

lw $a0 0($a0)push $a0 // push e1
li $a0 0
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0

bc $a0 LABELthen1
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
//Start codegen of ast.node.exp.LhsExpNode-ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id x

lw $a0 0($a0)push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al 1 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 x=exp

push $fp
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id x

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 2($al) //put in $a0 value of Id y

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function1// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf2
LABELthen1:
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id x

free $a0 //free address in $a0

LABELendIf2:

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction1

endFunction1:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 2// pop parameters2
pop
lw $fp 0($sp)
pop
jr $ra
// END OF h

//FINE FUNZIONI

endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF quattro

//BEGIN FUNCTION Function2
Function2:
mv $sp $fp
push $ra
li $a0 1

push $a0

push $fp
li $a0 54

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function3// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction2
//CREO FUNZIONI
//BEGIN FUNCTION Function3
Function3:
mv $sp $fp
push $ra
//Start codegen of ast.node.exp.IdExpNode==ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id y
push $a0 // push e1
li $a0 0
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0

bc $a0 LABELthen3
push $fp
//Start codegen of ast.node.exp.IdExpNode-ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id y
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
push $al
jal  Function3// jump to start of function and put in $ra next istruction
b LABELendIf4
LABELthen3:
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 -2($al) //put in $a0 value of Id x

print $a0

LABELendIf4:

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction3

endFunction3:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF f

//FINE FUNZIONI

endFunction2:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF sei

//BEGIN FUNCTION Function4
Function4:
mv $sp $fp
push $ra
new $a0// put new address in a0

push $a0

li $a0 1

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -2 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 u=exp

mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id u

lw $a0 0($a0)
print $a0

push $fp
li $a0 6

push $a0
mv $fp $al //put in $al actual fp
lw $a0 -2($al) //put in $a0 value of Id u

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function5// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction4
//CREO FUNZIONI
//BEGIN FUNCTION Function5
Function5:
mv $sp $fp
push $ra
//Start codegen of ast.node.exp.IdExpNode==ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $a0 2($al) //put in $a0 value of Id n
push $a0 // push e1
li $a0 0
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
eq $a0 $a2 $a0 // $a0 = $a2 == $a0

bc $a0 LABELthen5
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
new $a0// put new address in a0

push $a0

//Start codegen of ast.node.exp.LhsExpNode*ast.node.exp.IdExpNode
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id x

lw $a0 0($a0)push $a0 // push e1
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 2($al) //put in $a0 value of Id n
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
mult $a0 $a2 $a0 // a0 = t1+a0

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 y=exp

push $fp
//Start codegen of ast.node.exp.IdExpNode-ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 2($al) //put in $a0 value of Id n
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
sub $a0 $a2 $a0 // a0 = t1-a0

push $a0
mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id y

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function5// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf6
LABELthen5:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id x

lw $a0 0($a0)
print $a0

mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id x

free $a0 //free address in $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf6:

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction5

endFunction5:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 2// pop parameters2
pop
lw $fp 0($sp)
pop
jr $ra
// END OF f

//FINE FUNZIONI

endFunction4:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF sette

//BEGIN FUNCTION Function6
Function6:
mv $sp $fp
push $ra
push $fp
li $a0 4

push $a0
li $a0 5

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function7// jump to start of function and put in $ra next istruction

push $fp
li $a0 5

push $a0
li $a0 4

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function7// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction6
//CREO FUNZIONI
//BEGIN FUNCTION Function7
Function7:
mv $sp $fp
push $ra
//Start codegen of ast.node.exp.IdExpNode>ast.node.exp.IdExpNode
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id m
push $a0 // push e1
mv $fp $al //put in $al actual fp
lw $a0 2($al) //put in $a0 value of Id n
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
gt $a0 $a2 $a0 // $a0 = $a2 > $a0

bc $a0 LABELthen7
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 1

push $a0

push $fp
//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 2($al) //put in $a0 value of Id n
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

push $a0
//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.single_exp.IntNode
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id m
push $a0 // push e1
li $a0 1
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function7// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf8
LABELthen7:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
//Start codegen of ast.node.exp.IdExpNode+ast.node.exp.IdExpNode
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id m
push $a0 // push e1
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 2($al) //put in $a0 value of Id n
lw $a2 0($sp) //take e2 and $a2 take e1
pop // remove e1 from the stack to preserve stack
add $a0 $a2 $a0 // a0 = t1+a0

print $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf8:

subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction7

endFunction7:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 2// pop parameters2
pop
lw $fp 0($sp)
pop
jr $ra
// END OF f

//FINE FUNZIONI

endFunction6:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 0// pop parameters0
pop
lw $fp 0($sp)
pop
jr $ra
// END OF otto

//FINE FUNZIONI
