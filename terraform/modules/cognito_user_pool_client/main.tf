resource "aws_cognito_user_pool_client" "client" {
  name = var.client_name
  user_pool_id = var.user_pool_id
  allowed_oauth_flows = ["code"]
  allowed_oauth_scopes = ["openid"]
  callback_urls = var.callbacks
  explicit_auth_flows = ["ALLOW_USER_PASSWORD_AUTH", "ALLOW_REFRESH_TOKEN_AUTH"]
  generate_secret = true
  supported_identity_providers = ["COGNITO"]
}
