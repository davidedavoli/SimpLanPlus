push 0
mv $sp $fp //Load new $fp
new $a0// put new address in a0

push $a0

li $a0 10

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id
lw $al 0($al) //deferencing inner

sw $a0 0($al) // 0($al) = $a0 id=exp 

push $fp
li $a0 1

push $a0
mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next istruction

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
mv $sp $fp
push $ra
mv $fp $al //put in $al actual fp
lw $a0 1($al) //put in $a0 value of Id

lw $a0 0($a0)
print $a0

mv $fp $al //put in $a1 (al) actual fp
addi $al $al 1 //put in $al address of Id

free $a0 //free address in $a0

mv $fp $al //put in $al actual fp
lw $a0 2($al) //put in $a0 value of Id

bc $a0 LABELthen1
b LABELendIf2
LABELthen1:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
push $fp
li $a0 0

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $a0 1($al) //put in $a0 value of Id

push $a0
mv $fp $al //put in $al actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
push $al
jal  Function0// jump to start of function and put in $ra next istruction

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:


subi $sp $fp 1 //Restore stackpointer as before block creation in a void function without return 
lw $fp 0($fp) //Load old $fp pushed 
endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 2// pop parameters2
pop
lw $fp 0($sp)
pop
jr $ra
// END OF g

//FINE FUNZIONI
