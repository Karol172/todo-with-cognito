variable "user_pool_id" {
  type = string
  description = "Id of the cognito user pool."
}

variable "client_name" {
  type = string
  description = "The name of client."
}

variable "callbacks" {
  type = list
  description = "List of URI callbacks"
}
