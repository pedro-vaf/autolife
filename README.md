<img width="1919" height="910" alt="login" src="https://github.com/user-attachments/assets/b8cc1d9d-2710-4ae5-bc99-9198aad96d53" />
<img width="1919" height="911" alt="medicoPage" src="https://github.com/user-attachments/assets/3855a46b-4cde-470b-ba45-96bad333fa28" />
<img width="1917" height="911" alt="pacientePage" src="https://github.com/user-attachments/assets/fca54d00-bb2b-4f83-b9de-0d9f9b33692b" />
<img width="1919" height="909" alt="consultaPage" src="https://github.com/user-attachments/assets/c3b78e3f-e436-4119-a4d0-1023eb9d7d17" />
<img width="1919" height="1022" alt="eureka" src="https://github.com/user-attachments/assets/8a997aad-74b9-4e79-ac66-762054a5e1db" />
<img width="1918" height="1023" alt="rabbitmq" src="https://github.com/user-attachments/assets/3bf4924f-9baf-4416-adbc-985c9effcc53" />

# AutoLife

Sistema hospitalar desenvolvido com arquitetura de microsserviços, utilizando **Java com Spring Boot** no backend e **React** no frontend.

O projeto foi construído com foco em escalabilidade, segurança, comunicação assíncrona e organização de domínio.

---

## Tecnologias Utilizadas

### Backend
- Java
- Spring Boot
- Spring Security
- JWT
- Spring Cloud Netflix (Eureka + Gateway)
- RabbitMQ

### Frontend
- React
- Fetch API

### Infraestrutura
- Docker
- Docker Compose

---

## 📌 Funcionalidades

- Cadastro de pacientes
- Cadastro de médicos
- Agendamento de consultas
- Cancelamento de consultas
- Envio de e-mails com template HTML
- Lembretes automáticos de consulta

---

## 📨 Arquitetura de Notificação

O sistema utiliza mensageria com RabbitMQ:

- **Notificacao Service** → responsável por publicar eventos
- **Email Service** → responsável por consumir eventos e realizar o envio de e-mails

Essa abordagem garante maior desacoplamento, resiliência e escalabilidade entre os serviços.

---

## 🐳 Como Executar o Projeto

Com todas as dependências instaladas na máquina, execute na raiz do projeto:

```bash
sudo docker compose up --build
