push 0
mv $sp $fp //Load new $fp
subi $sp $sp 1 // non assegnato nulla

push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
subi $sp $sp 1 // non assegnato nulla

li $a0 5

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

li $a0 1

bc $a0 LABELthen1
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 10

print $a0

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 
b LABELendIf2
LABELthen1:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 4

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf2:

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
subi $sp $sp 1 // non assegnato nulla

li $a0 1

bc $a0 LABELthen3
b LABELendIf4
LABELthen3:
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 7

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

LABELendIf4:

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

li $a0 10

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
lw $al 0($al) //go up to chain
lw $al 0($al) //go up to chain
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($al) = $a0 id=exp 

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode
lw $fp 0($fp) //Load old $fp pushed 

mv $fp $al //put in $al actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

halt
