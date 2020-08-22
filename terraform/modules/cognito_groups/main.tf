resource "aws_cognito_user_group" "admin" {
  name = "admin"
  user_pool_id = var.user_pool_id
}

resource "aws_cognito_user_group" "users" {
  name = "users"
  user_pool_id = var.user_pool_id
}
