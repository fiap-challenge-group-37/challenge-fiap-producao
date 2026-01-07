# language: pt
Funcionalidade: Gestão da Fila de Produção
  Como um cozinheiro
  Eu quero visualizar e atualizar os pedidos na fila
  Para que eu possa preparar e entregar os lanches

  Cenario: Receber um novo pedido pago
    Dado que um pedido com ID 100 e itens "Hamburguer" foi pago
    Quando o worker processar a mensagem de pagamento
    Entao o pedido deve estar na fila com status "RECEBIDO"

  Cenario: Avançar status do pedido
    Dado que existe um pedido na fila com ID "100" e status "RECEBIDO"
    Quando eu atualizo o status do pedido "100" para "EM_PREPARACAO"
    Entao o pedido deve aparecer na lista com status "EM_PREPARACAO"