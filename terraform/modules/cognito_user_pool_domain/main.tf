resource "aws_cognito_user_pool_domain" "domain" {
  domain = var.domain
  user_pool_id = var.user_pool_id
}
