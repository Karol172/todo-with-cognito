output "urlRoot" {
  value = module.cognito_user_pool_domain.domain
}

output "clientId" {
  value = module.cognito_user_pool_client.client_id
}

output "clientSecret" {
  value = module.cognito_user_pool_client.client_secret
}

output "poolId" {
  value = module.cognito_user_pool.id
}

output "admin_credentials" {
  value = module.cognito_users.admin_credentials
}


output "user_credentials" {
  value = module.cognito_users.user_credentials
}