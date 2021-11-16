

veiculo(bicicleta).
veiculo(mota).
veiculo(carro).

%encomenda(Peso,Volume,Morada).

encomenda(5 , 8 , "Rua Santo António, nº420").
encomenda(14, 10, "Praça do Comércio, nº15").
encomenda(9 , 5 , "Entrocamento de São Geraldes, nº30").
encomenda(6 , 6 , "Rua José Sócrates, nº2").
encomenda(4 , 13, "Rua Monte Carrinhos , nº45").
encomenda(12, 9 , "Rua Paulo Fernandes , nº48").
encomenda(3 , 4 , "Praça Arsenalistas  , nº410").
encomenda(10, 12, "Rua do Souto , nº13").
encomenda(9 , 3 , "Rua Santa Margarida , nº 60").
encomenda(3 , 2 , "Largo Sr. dos Aflitos, nº 4").
encomenda(14, 3 , "Rua do Raio, nº142").
encomenda(2 , 4 , "Rua da Avenida da Liberdade, nº 60").



morada(Rua,Freguesia,Cidade).


maxSpeed(bicicleta,10).
maxSpeed(carro,25).
maxSpeed(mota,35).

maxWeight(bicicleta,5).
maxWeight(carro,100).
maxWeight(mota,20).


%Encomendas -> lista
estafeta(Veiculo,NumEnc,Rank,Encomendas).

