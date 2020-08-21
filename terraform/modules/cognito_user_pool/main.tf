resource "aws_cognito_user_pool" "my_user_pool" {
  name = var.name

  password_policy {
    minimum_length = 6
    require_lowercase = true
    require_numbers = true
  }

}