variable "user_pool_id" {
  type = string
  description = "Id of the user pool"
}

variable "group_admin" {
  type = string
  description = "The name of admin group."
}

variable "group_users" {
  type = string
  description = "The name of user group."
}

variable "user_password" {
  type = string
  description = "The password for all users."
}
