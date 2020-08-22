provider "aws" {
  region = "eu-central-1"
}

module "cognito_user_pool" {
  source = "../modules/cognito_user_pool"
  name = var.cognito_user_pool_name
}

module "cognito_groups" {
  source = "../modules/cognito_groups"
  user_pool_id = module.cognito_user_pool.id
}

module "cognito_users" {
  source = "../modules/cognito_users"
  group_admin = module.cognito_groups.admin_group
  group_users = module.cognito_groups.users_group
  user_password = var.cognito_users_password
  user_pool_id = module.cognito_user_pool.id
}

module "cognito_user_pool_domain" {
  source = "../modules/cognito_user_pool_domain"
  domain = var.cognito_domain
  user_pool_id = module.cognito_user_pool.id
}

module "cognito_user_pool_client" {
  source = "../modules/cognito_user_pool_client"
  callbacks = var.cognito_user_pool_client_callbacks
  client_name = var.cognito_user_pool_client_name
  user_pool_id = module.cognito_user_pool.id
}

data "aws_region" "current" {}
