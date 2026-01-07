# Configura o provedor da AWS
provider "aws" {
  region = "us-east-1" # Região padrão da FIAP/Labs (se for outra, altere aqui)
}

# 1. Cria a Fila Principal (Onde os pedidos chegam)
resource "aws_sqs_queue" "pedido_pago_queue" {
  name                      = "pedido-pago-queue"
  delay_seconds             = 0
  max_message_size          = 262144
  message_retention_seconds = 86400   # 1 dia
  receive_wait_time_seconds = 10      # Long polling (economiza dinheiro)

  # Configura a fila de erros (Dead Letter Queue) - Opcional mas recomendado
  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.pedido_pago_dlq.arn
    maxReceiveCount     = 5 # Tenta 5 vezes antes de desistir
  })
}

# 2. Cria a Fila de Erros (Para onde vão as mensagens que derem pau)
resource "aws_sqs_queue" "pedido_pago_dlq" {
  name = "pedido-pago-dlq"
}

# 3. Outputs (Mostra essas infos no terminal depois de criar)
output "queue_url" {
  value = aws_sqs_queue.pedido_pago_queue.url
}

output "queue_name" {
  value = aws_sqs_queue.pedido_pago_queue.name
}