variable "cognito_domain" {
  type = string
  description = "The domain of cognito user pool."
  default = "default-domain123123"
}

variable "cognito_user_pool_client_callbacks" {
  type = list
  description = "The list of Cognito callbacks."
  default = ["http://localhost:8080/auth/token"]
}

variable "cognito_user_pool_client_name" {
  type = string
  description = "The name of cognito user pool client."
  default = "spring-client"
}

variable "cognito_user_pool_name" {
  type = string
  description = "The name of cognito user pool."
  default = "my-user-pool"
}

variable "cognito_users_password" {
  type = string
  description = "Default password for all users."
  default = "secret123"
}
