push 0
mv $sp $fp //Load new $fp
push $fp
li $a0 1

push $a0
mv $fp $al //put in $al actual fp
push $al
jal  Function0// jump to start of function and put in $ra next istruction

print $a0

push $fp
li $a0 0

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
mv $fp $al //put in $a1 (al) actual fp
lw $a0 1($al) //put in $a0 value of Id

bc $a0 LABELthen1
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 0

push $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf2
LABELthen1:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 1

push $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

subi $sp $fp 1 //Restore stackpointer as before block creation in return 
lw $fp 0($fp) //Load old $fp pushed 
b endFunction0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:


endFunction0:
lw $ra 0($sp)
pop
addi $sp $sp 0//pop declaration 0
addi $sp $sp 1// pop parameters1
pop
lw $fp 0($sp)
pop
jr $ra
// END OF provaif

//FINE FUNZIONI
