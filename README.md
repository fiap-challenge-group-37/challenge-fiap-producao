# 🧑‍🍳 Microsserviço de Produção (Cozinha)
Este microsserviço é responsável pela gestão da fila de preparação de pedidos da lanchonete. Ele organiza os pedidos por prioridade de status e garante que apenas pedidos ativos (não finalizados) apareçam para a cozinha.

#  🚀 Tecnologias e Infraestrutura
Java 21 & Spring Boot 3.3.1

dynamodbDB: Persistência de pedidos em produção.

AWS SQS: Integração assíncrona para recebimento de pedidos pagos.

Docker: Containerização do serviço e do banco de dados.

# ⚙️ Configuração Local (Docker)
Para subir o ambiente completo (API + dynamodbDB), execute:

Bash

docker-compose up -d --build
A API estará disponível em: http://localhost:8082

# 🔌 API Endpoints (Swagger)
A documentação interativa da API (Swagger UI) pode ser acedida em: 👉 http://localhost:8082/swagger-ui/index.html

Principais Operações:
Listar Fila: GET /producao/fila - Retorna pedidos ordenados por status (Pronto > Em Preparação > Recebido). Pedidos Finalizados são omitidos automaticamente.

Atualizar Status: PATCH /producao/{id}/status - Atualiza a etapa do pedido.

# 🧪 Teste Manual de Fluxo (PowerShell)
Para validar a regra de negócio onde um pedido sai da fila ao ser finalizado, siga estes passos no terminal:

1. Inserir Pedido Simulado
   Crie um pedido diretamente no dynamodbDB (simulando um evento vindo do SQS):

PowerShell

docker exec producao-dynamodb dynamodbsh fiap-producao-db --eval 'db.pedidos_cozinha.insertOne({ \"_id\": \"pedido-teste-01\", \"idPedidoOriginal\": 123, \"status\": \"RECEBIDO\", \"itens\": [{ \"nome\": \"Hambúrguer\", \"quantidade\": 1 }], \"dataEntrada\": new Date() })'
2. Consultar Fila Ativa
   Verifique se o pedido aparece na lista:

PowerShell

Invoke-RestMethod -Method Get -Uri "http://localhost:8082/producao/fila"
3. Finalizar o Pedido
   Altere o status para FINALIZADO:

PowerShell

Invoke-RestMethod -Method Patch -Uri "http://localhost:8082/producao/pedido-teste-01/status" -ContentType "application/json" -Body '{"status": "FINALIZADO"}'
4. Validar Saída da Fila
   Consulte a fila novamente. O resultado deve ser vazio []:

PowerShell

Invoke-RestMethod -Method Get -Uri "http://localhost:8082/producao/fila"
# ☁️ Deploy e Produção (Kubernetes)
O serviço está preparado para ambientes de cloud (AWS) via Kubernetes. As configurações de SQS são dinâmicas:

Local: O SQS inicia desligado (SQS_ENABLED: false) para evitar erros de credenciais AWS.

Nuvem: Através do k8s/configmap.yaml, as variáveis SQS_ENABLED e QUEUE_PEDIDO_PAGO ativam o consumo real das filas SQS.

# 🛠️ Comandos Úteis de Diagnóstico
PowerShell

Ver logs da aplicação:
docker logs producao-app

Reiniciar apenas a API:
docker restart producao-app

Reconstruir imagem ignorando cache:
docker-compose build --no-cache app