push 0
mv $sp $fp //Load new $fp
push $fp // save fp before call function
push $sp // save sp before call function
mv $sp $bsp
addi $a0 $bsp 2
sw $a0 0($bsp)
subi $sp $sp 1 //RA
mv $fp $al //put in $al actual fp
push $al // save access link
mv $sp $fp //update $fp
addi $fp $fp 0 // $fp at beginning of param
jal  Function0// jump to start of function and put in $ra next istruction

push $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

print $a0

halt
//CREO FUNZIONI
//BEGIN FUNCTION Function0
Function0:
sw $ra -1($bsp) //save ra before old sp
li $a0 5

push $a0
li $a0 4

push $a0
//BEGIN FUNCTION Function1
Function1:
sw $ra -1($bsp) //save ra before old sp
li $a0 8

push $a0
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 8

push $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

b endFunction1

subi $sp $fp 1 //Restore stackpointer as before block creation 
lw $fp 0($fp) //Load old $fp pushed 
//CREO FUNZIONI

endFunction1:
addi $sp $sp 1
addi $sp $sp 0
lw $ra -1($bsp) // load ra
lw $fp 1($bsp)
lw $sp 0($bsp)
addi $bsp $fp 2
jr $ra
// END OF f
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 5

push $a0

li $a0 4

push $a0

li $a0 7

//RITORNATO DA CGEN EXP
mv $fp $al //put in $a1 (al) actual fp
addi $al $al -1 //put in $al address of Id

sw $a0 0($al) // 0($a1) = $a0 id=exp 

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

b endFunction0

subi $sp $fp 1 //Restore stackpointer as before block creation 
lw $fp 0($fp) //Load old $fp pushed 
//CREO FUNZIONI
//BEGIN FUNCTION Function2
Function2:
sw $ra -1($bsp) //save ra before old sp
li $a0 8

push $a0
push 0
push $fp //loadind new block
mv $sp $fp //Load new $fp
li $a0 8

push $a0

mv $fp $al //put in $a1 (al) actual fp
lw $a0 -1($al) //put in $a0 value of Id

b endFunction2

subi $sp $fp 1 //Restore stackpointer as before block creation 
lw $fp 0($fp) //Load old $fp pushed 
//CREO FUNZIONI

endFunction2:
addi $sp $sp 1
addi $sp $sp 0
lw $ra -1($bsp) // load ra
lw $fp 1($bsp)
lw $sp 0($bsp)
addi $bsp $fp 2
jr $ra
// END OF f


endFunction0:
addi $sp $sp 3
addi $sp $sp 0
lw $ra -1($bsp) // load ra
lw $fp 1($bsp)
lw $sp 0($bsp)
addi $bsp $fp 2
jr $ra
// END OF prova

