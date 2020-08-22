output "admin_group" {
  value = aws_cognito_user_group.admin.name
}

output "users_group" {
  value = aws_cognito_user_group.users.name
}
