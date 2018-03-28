# ana-message-logger
Pushes every client message received at api-gateway to messaging queue asynchronously, which will be eventually stored in chat history database

Currently only supports AWS SQS, eventually planning to support other open source message queues.
